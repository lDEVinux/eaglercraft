package net.minecraft.src;

public abstract class NetHandler {
	/**
	 * determine if it is a server handler
	 */
	public abstract boolean isServerHandler();

	/**
	 * Handle Packet51MapChunk (full chunk update of blocks, metadata, light levels,
	 * and optionally biome data)
	 */
	public void handleMapChunk(Packet51MapChunk par1Packet51MapChunk) {
	}
	
	public void handlePackets() {
	}
	
	public boolean shouldBeRemoved() {
		return true;
	}

	/**
	 * Default handler called for packets that don't have their own handlers in
	 * NetServerHandler; kicks player from the server.
	 */
	public void unexpectedPacket(Packet par1Packet) {
	}

	public void handleErrorMessage(String par1Str, Object[] par2ArrayOfObj) {
	}

	public void handleKickDisconnect(Packet255KickDisconnect par1Packet255KickDisconnect) {
		this.unexpectedPacket(par1Packet255KickDisconnect);
	}

	public void handleLogin(Packet1Login par1Packet1Login) {
		this.unexpectedPacket(par1Packet1Login);
	}

	public void handleFlying(Packet10Flying par1Packet10Flying) {
		this.unexpectedPacket(par1Packet10Flying);
	}

	public void handleMultiBlockChange(Packet52MultiBlockChange par1Packet52MultiBlockChange) {
		this.unexpectedPacket(par1Packet52MultiBlockChange);
	}

	public void handleBlockDig(Packet14BlockDig par1Packet14BlockDig) {
		this.unexpectedPacket(par1Packet14BlockDig);
	}

	public void handleBlockChange(Packet53BlockChange par1Packet53BlockChange) {
		this.unexpectedPacket(par1Packet53BlockChange);
	}

	public void handleNamedEntitySpawn(Packet20NamedEntitySpawn par1Packet20NamedEntitySpawn) {
		this.unexpectedPacket(par1Packet20NamedEntitySpawn);
	}

	public void handleEntity(Packet30Entity par1Packet30Entity) {
		this.unexpectedPacket(par1Packet30Entity);
	}

	public void handleEntityTeleport(Packet34EntityTeleport par1Packet34EntityTeleport) {
		this.unexpectedPacket(par1Packet34EntityTeleport);
	}

	public void handlePlace(Packet15Place par1Packet15Place) {
		this.unexpectedPacket(par1Packet15Place);
	}

	public void handleBlockItemSwitch(Packet16BlockItemSwitch par1Packet16BlockItemSwitch) {
		this.unexpectedPacket(par1Packet16BlockItemSwitch);
	}

	public void handleDestroyEntity(Packet29DestroyEntity par1Packet29DestroyEntity) {
		this.unexpectedPacket(par1Packet29DestroyEntity);
	}

	public void handleCollect(Packet22Collect par1Packet22Collect) {
		this.unexpectedPacket(par1Packet22Collect);
	}

	public void handleChat(Packet3Chat par1Packet3Chat) {
		this.unexpectedPacket(par1Packet3Chat);
	}

	public void handleVehicleSpawn(Packet23VehicleSpawn par1Packet23VehicleSpawn) {
		this.unexpectedPacket(par1Packet23VehicleSpawn);
	}

	public void handleAnimation(Packet18Animation par1Packet18Animation) {
		this.unexpectedPacket(par1Packet18Animation);
	}

	/**
	 * runs registerPacket on the given Packet19EntityAction
	 */
	public void handleEntityAction(Packet19EntityAction par1Packet19EntityAction) {
		this.unexpectedPacket(par1Packet19EntityAction);
	}

	public void handleClientProtocol(Packet2ClientProtocol par1Packet2ClientProtocol) {
		this.unexpectedPacket(par1Packet2ClientProtocol);
	}

	public void handleMobSpawn(Packet24MobSpawn par1Packet24MobSpawn) {
		this.unexpectedPacket(par1Packet24MobSpawn);
	}

	public void handleUpdateTime(Packet4UpdateTime par1Packet4UpdateTime) {
		this.unexpectedPacket(par1Packet4UpdateTime);
	}

	public void handleSpawnPosition(Packet6SpawnPosition par1Packet6SpawnPosition) {
		this.unexpectedPacket(par1Packet6SpawnPosition);
	}

	/**
	 * Packet handler
	 */
	public void handleEntityVelocity(Packet28EntityVelocity par1Packet28EntityVelocity) {
		this.unexpectedPacket(par1Packet28EntityVelocity);
	}

	/**
	 * Packet handler
	 */
	public void handleEntityMetadata(Packet40EntityMetadata par1Packet40EntityMetadata) {
		this.unexpectedPacket(par1Packet40EntityMetadata);
	}

	/**
	 * Packet handler
	 */
	public void handleAttachEntity(Packet39AttachEntity par1Packet39AttachEntity) {
		this.unexpectedPacket(par1Packet39AttachEntity);
	}

	public void handleUseEntity(Packet7UseEntity par1Packet7UseEntity) {
		this.unexpectedPacket(par1Packet7UseEntity);
	}

	/**
	 * Packet handler
	 */
	public void handleEntityStatus(Packet38EntityStatus par1Packet38EntityStatus) {
		this.unexpectedPacket(par1Packet38EntityStatus);
	}

	/**
	 * Recieves player health from the server and then proceeds to set it locally on
	 * the client.
	 */
	public void handleUpdateHealth(Packet8UpdateHealth par1Packet8UpdateHealth) {
		this.unexpectedPacket(par1Packet8UpdateHealth);
	}

	/**
	 * respawns the player
	 */
	public void handleRespawn(Packet9Respawn par1Packet9Respawn) {
		this.unexpectedPacket(par1Packet9Respawn);
	}

	public void handleExplosion(Packet60Explosion par1Packet60Explosion) {
		this.unexpectedPacket(par1Packet60Explosion);
	}

	public void handleOpenWindow(Packet100OpenWindow par1Packet100OpenWindow) {
		this.unexpectedPacket(par1Packet100OpenWindow);
	}

	public void handleCloseWindow(Packet101CloseWindow par1Packet101CloseWindow) {
		this.unexpectedPacket(par1Packet101CloseWindow);
	}

	public void handleWindowClick(Packet102WindowClick par1Packet102WindowClick) {
		this.unexpectedPacket(par1Packet102WindowClick);
	}

	public void handleSetSlot(Packet103SetSlot par1Packet103SetSlot) {
		this.unexpectedPacket(par1Packet103SetSlot);
	}

	public void handleWindowItems(Packet104WindowItems par1Packet104WindowItems) {
		this.unexpectedPacket(par1Packet104WindowItems);
	}

	/**
	 * Updates Client side signs
	 */
	public void handleUpdateSign(Packet130UpdateSign par1Packet130UpdateSign) {
		this.unexpectedPacket(par1Packet130UpdateSign);
	}

	public void handleUpdateProgressbar(Packet105UpdateProgressbar par1Packet105UpdateProgressbar) {
		this.unexpectedPacket(par1Packet105UpdateProgressbar);
	}

	public void handlePlayerInventory(Packet5PlayerInventory par1Packet5PlayerInventory) {
		this.unexpectedPacket(par1Packet5PlayerInventory);
	}

	public void handleTransaction(Packet106Transaction par1Packet106Transaction) {
		this.unexpectedPacket(par1Packet106Transaction);
	}

	/**
	 * Packet handler
	 */
	public void handleEntityPainting(Packet25EntityPainting par1Packet25EntityPainting) {
		this.unexpectedPacket(par1Packet25EntityPainting);
	}

	public void handleBlockEvent(Packet54PlayNoteBlock par1Packet54PlayNoteBlock) {
		this.unexpectedPacket(par1Packet54PlayNoteBlock);
	}

	/**
	 * runs registerPacket on the given Packet200Statistic
	 */
	public void handleStatistic(Packet200Statistic par1Packet200Statistic) {
		this.unexpectedPacket(par1Packet200Statistic);
	}

	public void handleSleep(Packet17Sleep par1Packet17Sleep) {
		this.unexpectedPacket(par1Packet17Sleep);
	}

	public void handleGameEvent(Packet70GameEvent par1Packet70GameEvent) {
		this.unexpectedPacket(par1Packet70GameEvent);
	}

	/**
	 * Handles weather packet
	 */
	public void handleWeather(Packet71Weather par1Packet71Weather) {
		this.unexpectedPacket(par1Packet71Weather);
	}

	/**
	 * Contains logic for handling packets containing arbitrary unique item data.
	 * Currently this is only for maps.
	 */
	public void handleMapData(Packet131MapData par1Packet131MapData) {
		this.unexpectedPacket(par1Packet131MapData);
	}

	public void handleDoorChange(Packet61DoorChange par1Packet61DoorChange) {
		this.unexpectedPacket(par1Packet61DoorChange);
	}

	/**
	 * Handle a server ping packet.
	 */
	public void handleServerPing(Packet254ServerPing par1Packet254ServerPing) {
		this.unexpectedPacket(par1Packet254ServerPing);
	}

	/**
	 * Handle an entity effect packet.
	 */
	public void handleEntityEffect(Packet41EntityEffect par1Packet41EntityEffect) {
		this.unexpectedPacket(par1Packet41EntityEffect);
	}

	/**
	 * Handle a remove entity effect packet.
	 */
	public void handleRemoveEntityEffect(Packet42RemoveEntityEffect par1Packet42RemoveEntityEffect) {
		this.unexpectedPacket(par1Packet42RemoveEntityEffect);
	}

	/**
	 * Handle a player information packet.
	 */
	public void handlePlayerInfo(Packet201PlayerInfo par1Packet201PlayerInfo) {
		this.unexpectedPacket(par1Packet201PlayerInfo);
	}

	/**
	 * Handle a keep alive packet.
	 */
	public void handleKeepAlive(Packet0KeepAlive par1Packet0KeepAlive) {
		this.unexpectedPacket(par1Packet0KeepAlive);
	}

	/**
	 * Handle an experience packet.
	 */
	public void handleExperience(Packet43Experience par1Packet43Experience) {
		this.unexpectedPacket(par1Packet43Experience);
	}

	/**
	 * Handle a creative slot packet.
	 */
	public void handleCreativeSetSlot(Packet107CreativeSetSlot par1Packet107CreativeSetSlot) {
		this.unexpectedPacket(par1Packet107CreativeSetSlot);
	}

	/**
	 * Handle a entity experience orb packet.
	 */
	public void handleEntityExpOrb(Packet26EntityExpOrb par1Packet26EntityExpOrb) {
		this.unexpectedPacket(par1Packet26EntityExpOrb);
	}

	public void handleEnchantItem(Packet108EnchantItem par1Packet108EnchantItem) {
	}

	public void handleCustomPayload(Packet250CustomPayload par1Packet250CustomPayload) {
	}

	public void handleEntityHeadRotation(Packet35EntityHeadRotation par1Packet35EntityHeadRotation) {
		this.unexpectedPacket(par1Packet35EntityHeadRotation);
	}

	public void handleTileEntityData(Packet132TileEntityData par1Packet132TileEntityData) {
		this.unexpectedPacket(par1Packet132TileEntityData);
	}

	/**
	 * Handle a player abilities packet.
	 */
	public void handlePlayerAbilities(Packet202PlayerAbilities par1Packet202PlayerAbilities) {
		this.unexpectedPacket(par1Packet202PlayerAbilities);
	}

	public void handleAutoComplete(Packet203AutoComplete par1Packet203AutoComplete) {
		this.unexpectedPacket(par1Packet203AutoComplete);
	}

	public void handleClientInfo(Packet204ClientInfo par1Packet204ClientInfo) {
		this.unexpectedPacket(par1Packet204ClientInfo);
	}

	public void handleLevelSound(Packet62LevelSound par1Packet62LevelSound) {
		this.unexpectedPacket(par1Packet62LevelSound);
	}

	public void handleBlockDestroy(Packet55BlockDestroy par1Packet55BlockDestroy) {
		this.unexpectedPacket(par1Packet55BlockDestroy);
	}

	public void handleClientCommand(Packet205ClientCommand par1Packet205ClientCommand) {
	}

	public void handleMapChunks(Packet56MapChunks par1Packet56MapChunks) {
		this.unexpectedPacket(par1Packet56MapChunks);
	}

	/**
	 * If this returns false, all packets will be queued for the main thread to
	 * handle, even if they would otherwise be processed asynchronously. Used to
	 * avoid processing packets on the client before the world has been downloaded
	 * (which happens on the main thread)
	 */
	public boolean canProcessPacketsAsync() {
		return false;
	}

	/**
	 * Handle a set objective packet.
	 */
	public void handleSetObjective(Packet206SetObjective par1Packet206SetObjective) {
		this.unexpectedPacket(par1Packet206SetObjective);
	}

	/**
	 * Handle a set score packet.
	 */
	public void handleSetScore(Packet207SetScore par1Packet207SetScore) {
		this.unexpectedPacket(par1Packet207SetScore);
	}

	/**
	 * Handle a set display objective packet.
	 */
	public void handleSetDisplayObjective(Packet208SetDisplayObjective par1Packet208SetDisplayObjective) {
		this.unexpectedPacket(par1Packet208SetDisplayObjective);
	}

	/**
	 * Handle a set player team packet.
	 */
	public void handleSetPlayerTeam(Packet209SetPlayerTeam par1Packet209SetPlayerTeam) {
		this.unexpectedPacket(par1Packet209SetPlayerTeam);
	}

	/**
	 * Handle a world particles packet.
	 */
	public void handleWorldParticles(Packet63WorldParticles par1Packet63WorldParticles) {
		this.unexpectedPacket(par1Packet63WorldParticles);
	}
}
