// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee;

import java.beans.ConstructorProperties;
import java.security.PublicKey;
import java.util.Objects;
import java.util.Queue;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import io.netty.channel.ChannelHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.score.Objective;
import net.md_5.bungee.api.score.Scoreboard;
import net.md_5.bungee.api.score.Team;
import net.md_5.bungee.connection.CancelSendSignal;
import net.md_5.bungee.connection.DownstreamBridge;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.CipherDecoder;
import net.md_5.bungee.netty.CipherEncoder;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PacketDecoder;
import net.md_5.bungee.netty.PacketHandler;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.Forge;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import net.md_5.bungee.protocol.packet.Packet1Login;
import net.md_5.bungee.protocol.packet.Packet9Respawn;
import net.md_5.bungee.protocol.packet.PacketCEScoreboardObjective;
import net.md_5.bungee.protocol.packet.PacketD1Team;
import net.md_5.bungee.protocol.packet.PacketFAPluginMessage;
import net.md_5.bungee.protocol.packet.PacketFCEncryptionResponse;
import net.md_5.bungee.protocol.packet.PacketFDEncryptionRequest;
import net.md_5.bungee.protocol.packet.PacketFFKick;
import net.md_5.bungee.protocol.packet.forge.Forge1Login;

public class ServerConnector extends PacketHandler
{
    private final ProxyServer bungee;
    private ChannelWrapper ch;
    private final UserConnection user;
    private final BungeeServerInfo target;
    private State thisState;
    private SecretKey secretkey;
    private boolean sentMessages;
    private boolean protocolSupport = BungeeCord.getInstance().config.getProtocolSupport();
    
    @Override
    public void exception(final Throwable t) throws Exception {
        final String message = "Exception Connecting:" + Util.exception(t);
        if (this.user.getServer() == null || this.user.getServer().getInfo() == null) {
            this.user.disconnect(message);
        }
        else {
            this.user.sendMessage(ChatColor.RED + message);
        }
    }
    
    @Override
    public void connected(final ChannelWrapper channel) throws Exception {
        this.ch = channel;
        channel.write(this.user.getPendingConnection().getHandshake());
        if (!protocolSupport) {
            final ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Login");
            out.writeUTF(this.user.getAddress().getHostString());
            out.writeInt(this.user.getAddress().getPort());
            channel.write(new PacketFAPluginMessage("BungeeCord", out.toByteArray()));
            if (this.user.getPendingConnection().getForgeLogin() == null) {
                channel.write(PacketConstants.CLIENT_LOGIN);
            }
        }
    }
    
    @Override
    public void disconnected(final ChannelWrapper channel) throws Exception {
        this.user.getPendingConnects().remove(this.target);
    }
    
    @Override
    public void handle(final Packet1Login login) throws Exception {
        Preconditions.checkState(this.thisState == State.LOGIN || this.thisState == State.ENCRYPT_REQUEST, (Object)"Not expecting LOGIN/ENCRYPT_REQUEST");
        if (this.thisState == State.ENCRYPT_REQUEST) {
            this.thisState = State.LOGIN;
        }
        final ServerConnection server = new ServerConnection(this.ch, this.target);
        final ServerConnectedEvent event = new ServerConnectedEvent(this.user, server);
        this.bungee.getPluginManager().callEvent(event);
        this.ch.write(BungeeCord.getInstance().registerChannels());
        final Queue<DefinedPacket> packetQueue = this.target.getPacketQueue();
        synchronized (packetQueue) {
            while (!packetQueue.isEmpty()) {
                this.ch.write(packetQueue.poll());
            }
        }
        for (final PacketFAPluginMessage message : this.user.getPendingConnection().getRegisterMessages()) {
            this.ch.write(message);
        }
        if (!this.sentMessages) {
            for (final PacketFAPluginMessage message : this.user.getPendingConnection().getLoginMessages()) {
                this.ch.write(message);
            }
        }
        if (this.user.getSettings() != null) {
            this.ch.write(this.user.getSettings());
        }
        synchronized (this.user.getSwitchMutex()) {
            if (this.user.getServer() == null) {
                this.user.setClientEntityId(login.getEntityId());
                this.user.setServerEntityId(login.getEntityId());
                Packet1Login modLogin;
                if (((PacketDecoder)this.ch.getHandle().pipeline().get((Class)PacketDecoder.class)).getProtocol() == Forge.getInstance()) {
                    modLogin = new Forge1Login(login.getEntityId(), login.getLevelType(), login.getGameMode(), login.getDimension(), login.getDifficulty(), login.getUnused(), (byte)this.user.getPendingConnection().getListener().getTabListSize());
                }
                else {
                    modLogin = new Packet1Login(login.getEntityId(), login.getLevelType(), login.getGameMode(), (byte)login.getDimension(), login.getDifficulty(), login.getUnused(), (byte)this.user.getPendingConnection().getListener().getTabListSize());
                }
                this.user.unsafe().sendPacket(modLogin);
            }
            else {
                this.user.getTabList().onServerChange();
                final Scoreboard serverScoreboard = this.user.getServerSentScoreboard();
                for (final Objective objective : serverScoreboard.getObjectives()) {
                    this.user.unsafe().sendPacket(new PacketCEScoreboardObjective(objective.getName(), objective.getValue(), (byte)1));
                }
                for (final Team team : serverScoreboard.getTeams()) {
                    this.user.unsafe().sendPacket(new PacketD1Team(team.getName()));
                }
                serverScoreboard.clear();
                this.user.sendDimensionSwitch();
                this.user.setServerEntityId(login.getEntityId());
                this.user.unsafe().sendPacket(new Packet9Respawn(login.getDimension(), login.getDifficulty(), login.getGameMode(), (short)256, login.getLevelType()));
                this.user.getServer().setObsolete(true);
                this.user.getServer().disconnect("Quitting");
            }
            if (!this.user.isActive()) {
                server.disconnect("Quitting");
                this.bungee.getLogger().warning("No client connected for pending server!");
                return;
            }
            this.target.addPlayer(this.user);
            this.user.getPendingConnects().remove(this.target);
            this.user.setServer(server);
            ((HandlerBoss)this.ch.getHandle().pipeline().get((Class)HandlerBoss.class)).setHandler(new DownstreamBridge(this.bungee, this.user, server));
        }
        this.bungee.getPluginManager().callEvent(new ServerSwitchEvent(this.user));
        this.thisState = State.FINISHED;
        throw new CancelSendSignal();
    }
    
    @Override
    public void handle(final PacketFDEncryptionRequest encryptRequest) throws Exception {
        Preconditions.checkState(this.thisState == State.ENCRYPT_REQUEST, (Object)"Not expecting ENCRYPT_REQUEST");
        if (protocolSupport) {
            final ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Login");
            out.writeUTF(this.user.getAddress().getHostString());
            out.writeInt(this.user.getAddress().getPort());
            this.user.unsafe().sendPacket(new PacketFAPluginMessage("BungeeCord", out.toByteArray()));
            if (this.user.getPendingConnection().getForgeLogin() == null) {
                this.user.unsafe().sendPacket(PacketConstants.CLIENT_LOGIN);
            }
        }
        if (this.user.getPendingConnection().getForgeLogin() != null) {
            final PublicKey publickey = EncryptionUtil.getPubkey(encryptRequest);
            this.secretkey = EncryptionUtil.getSecret();
            final byte[] shared = EncryptionUtil.encrypt(publickey, this.secretkey.getEncoded());
            final byte[] token = EncryptionUtil.encrypt(publickey, encryptRequest.getVerifyToken());
            this.ch.write(new PacketFCEncryptionResponse(shared, token));
            final Cipher encrypt = EncryptionUtil.getCipher(1, this.secretkey);
            this.ch.getHandle().pipeline().addBefore(PipelineUtils.PACKET_DECODE_HANDLER, PipelineUtils.ENCRYPT_HANDLER, (ChannelHandler)new CipherEncoder(encrypt));
            this.thisState = State.ENCRYPT_RESPONSE;
        }
        else {
            this.thisState = State.LOGIN;
        }
    }
    
    @Override
    public void handle(final PacketFCEncryptionResponse encryptResponse) throws Exception {
        Preconditions.checkState(this.thisState == State.ENCRYPT_RESPONSE, (Object)"Not expecting ENCRYPT_RESPONSE");
        final Cipher decrypt = EncryptionUtil.getCipher(2, this.secretkey);
        this.ch.getHandle().pipeline().addBefore(PipelineUtils.PACKET_DECODE_HANDLER, PipelineUtils.DECRYPT_HANDLER, (ChannelHandler)new CipherDecoder(decrypt));
        this.ch.write(this.user.getPendingConnection().getForgeLogin());
        this.ch.write(PacketConstants.CLIENT_LOGIN);
        this.thisState = State.LOGIN;
    }
    
    @Override
    public void handle(final PacketFFKick kick) throws Exception {
        ServerInfo def = this.bungee.getServerInfo(this.user.getPendingConnection().getListener().getFallbackServer());
        if (Objects.equals(this.target, def)) {
            def = null;
        }
        final ServerKickEvent event = this.bungee.getPluginManager().callEvent(new ServerKickEvent(this.user, kick.getMessage(), def));
        if (event.isCancelled() && event.getCancelServer() != null) {
            this.user.connect(event.getCancelServer());
            return;
        }
        final String message = this.bungee.getTranslation("connect_kick") + this.target.getName() + ": " + kick.getMessage();
        if (this.user.getServer() == null || this.user.getServer().getInfo() == null) {
            this.user.disconnect(message);
        }
        else {
            this.user.sendMessage(message);
        }
    }
    
    @Override
    public void handle(final PacketFAPluginMessage pluginMessage) throws Exception {
        if (pluginMessage.equals(PacketConstants.I_AM_BUNGEE) && !BungeeCord.getInstance().config.allowBungeeOnBungee()) {
            throw new IllegalStateException("May not connect to another BungeeCord!");
        }
        if (pluginMessage.getTag().equals("FML") && (pluginMessage.getData()[0] & 0xFF) == 0x0) {
            final ByteArrayDataInput in = ByteStreams.newDataInput(pluginMessage.getData());
            in.readUnsignedByte();
            for (int count = in.readInt(), i = 0; i < count; ++i) {
                in.readUTF();
            }
            if (in.readByte() != 0) {
                ((PacketDecoder)this.ch.getHandle().pipeline().get((Class)PacketDecoder.class)).setProtocol(Forge.getInstance());
            }
        }
        this.user.unsafe().sendPacket(pluginMessage);
        if (!this.sentMessages && this.user.getPendingConnection().getForgeLogin() != null) {
            for (final PacketFAPluginMessage message : this.user.getPendingConnection().getLoginMessages()) {
                this.ch.write(message);
            }
            this.sentMessages = true;
        }
    }
    
    @Override
    public String toString() {
        return "[" + this.user.getName() + "] <-> ServerConnector [" + this.target.getName() + "]";
    }
    
    @ConstructorProperties({ "bungee", "user", "target" })
    public ServerConnector(final ProxyServer bungee, final UserConnection user, final BungeeServerInfo target) {
        this.thisState = State.ENCRYPT_REQUEST;
        this.bungee = bungee;
        this.user = user;
        this.target = target;
    }
    
    private enum State
    {
        ENCRYPT_REQUEST, 
        ENCRYPT_RESPONSE, 
        LOGIN, 
        FINISHED;
    }
}
