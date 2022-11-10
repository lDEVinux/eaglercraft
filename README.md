# This repository will be deleted imminently

### Create a fresh fork ASAP to preserve it, you MUST fork a 100% fresh copy in order for the repository to be considered genuine

### Please read `LAX1DUDE_SIGNATURE.txt` for instructions to verify this is an original copy, if the file is not present then do not use this copy of the project

### LAX1DUDE's PGP key is here: [https://deev.is/certs/LAX1DUDE_eagler_public.asc](https://deev.is/certs/LAX1DUDE_eagler_public.asc)

### Do not edit this README, modifying any file will invalidate the repository's checksum

### Download your worlds off of [https://g.deev.is/eaglercraft/](https://g.deev.is/eaglercraft/) and alts in case of a URL change for the demo client

### MY LAN WORLD RELAYS (relay.deev.is, relay.lax1dude.net) WILL REMAIN ONLINE FOR CONVENIENCE

# Eaglercraft

![eaglercraft](https://g.deev.is/eaglercraft/cover.png)
:-:
Eaglercraft is real Minecraft 1.5.2 that you can play in any regular web browser. That includes school chromebooks, it works on all chromebooks. It supports both singleplayer and multiplayer.

**Currently maintained by [ayunami2000](https://github.com/ayunami2000)**

**For any questions you can join the discord server and hit us up there [https://discord.gg/Ekzcgs3DKZ](https://discord.gg/Ekzcgs3DKZ)**

# Quick Start

### Client: [https://g.deev.is/eaglercraft/](https://g.deev.is/eaglercraft/) [^1]

### Offline Client Download: [Offline_Download_Version.html](https://github.com/lax1dude/eaglercraft/raw/main/stable-download/Offline_Download_Version.html)

**(right click the link and press 'Save link as...' to download the file)**

### Play Minecraft Beta Singleplayer: [https://g.deev.is/eaglercraft/beta/](https://g.deev.is/eaglercraft/beta/)

[^1]: A list of public servers are already added into the official client

# Table Of Contents:
| [Singleplayer](#Singleplayer)                                 | [Multiplayer](#Multiplayer)                                                     | [Others](#Others)                                     |
|---------------------------------------------------------------|---------------------------------------------------------------------------------|-------------------------------------------------------|
| [Importing and Exporting Worlds](#Importing-and-Exporting-Worlds) | [Public clients and servers](#Public-clients-and-servers)                           | [Plugin Development](#Plugin-Development)             |
| [LAN Worlds](#LAN-Worlds)                                     | [Creating a Server - Bukkit](#Creating-a-server---Bukkit)                       | [Compiling](#Compiling)                               |
| [Public LAN Relays](#Public-LAN-Relays)                       | [Creating a Server - EaglercraftBungee](#Creating-a-server---EaglercraftBungee) | [Creating a resource pack](#Creating-a-resource-pack) |
| [Creating a LAN Relay](#Creating-a-LAN-Relay)                 | [Creating a Client](#Creating-a-Client)                                         | [Contributing](#Contributing)                         |
|                                                               | [EaglercraftBungee Configuration](#EaglercraftBungee-Configuration)             |                                                       |
|                                                               | [Creating a Reverse Proxy - NGINX](#Creating-a-Reverse-Proxy---NGINX)           |                                                       |
|                                                               | [NGINX Configuration](#NGINX-Configuration)                                     |                                                       |

# Singleplayer

Simply press the 'Singleplayer' button on the main menu and you can create a regular vanilla minecraft and play it any time.

## Importing and Exporting Worlds
The worlds are stored in your browser's local storage, **you can export them as EPK files and import them again on all other Eaglercraft sites that also support singleplayer.** You can even copy an exported world to an entirely different computer, or send it to a friend, and import it and continue playing with all your progress saved.

## LAN Worlds

### Eaglercraft fully supports LAN worlds, you can share your world with any player and they can connect directly to it as if you are running a server in your browser.

**LAN worlds will work between any two devices connected to the internet, you are not limited to only players connected to your Wi-Fi network**

To open your world to LAN, go to the pause menu and click 'Open to LAN'. You can configure the gamemode and cheats and if you would like to hide your LAN world. **When you do not hide your LAN world, it will appear on the Multiplayer screen from the main menu to anybody else also on your Wi-Fi network.** Set the world hidden if you are at school or something and don't want everyone else in your class to join as well and start griefing.

When you open the world to LAN it will give you a 'join code'. Simply share the code with your friends and they can visit the Multiplayer screen from the main menu and click 'Direct Connect' and enter the code and they will be able to join your world.

Make sure they add the relay server your game opens the LAN world on to their "Network Settings" menu accessable from the Multiplayer screen. You simply must send them the URL indicated in the pause menu once the world is opened and they can use the "Add Relay" option to add the URL to their list.

### THIS IS A REQUIRED STEP FOR A PERSON TO JOIN YOUR WORLD, IF THEY DO NOT HAVE THE RELAY YOUR WORLD IS HOSTED ON ADDED TO THEIR "Network Settings" THE GAME WILL BE UNABLE TO LOCATE THE WORLD

## Public LAN Relays

### Here are some public relay servers you can use:

 - `wss://relay.deev.is/`
 - `wss://relay.lax1dude.net/`
 - `wss://relay.shhnowisnottheti.me/`

## Creating a LAN Relay

### Simply download [stable-download/sp-relay.jar](https://github.com/lax1dude/eaglercraft/blob/main/stable-download/sp-relay.jar) and run `java -jar sp-relay.jar`

**Run `java -jar sp-relay.jar --debug` to view debug info like all the IPs of incoming connections, as it is not shown by default because logging all that info will reduce performance when the relay is being pinged many times a second depending on it's popularity.**

Edit the `relayConfig.ini` file generated on first launch to change the port and configure ratelimiting and such, and `relays.txt` to change the list of STUN and TURN relays reported to clients connecting to the relay, which are required to correctly establish a P2P LAN world connection in browsers

**The `origin-whitelist` config variable is a semicolon (`;`) seperated list of domains used to restrict what sites are to be allowed to use your relay. When left blank it allows all sites. Add `offline` to allow offline download clients to use your relay as well, and `null` to allow connections that do not specify an `Origin:` header. Use `*` as a wildcard, for example: `*.deev.is` allows all domains ending with "deev.is" to use the relay.**

# Multiplayer
Multiplayer functions like vanilla Minecraft, allowing you to join normal Minecraft servers like a normal client.

## Public clients and servers

### There are multiple official clients hosted by lax1dude, here is a small list:
- Main: [https://g.deev.is/](https://g.deev.is/ "https://g.deev.is/")
- [https://eaglercraft.net/](https://eaglercraft.net/ "https://eaglercraft.net/")  
- [https://eaglercraft.org/](https://eaglercraft.org/ "https://eaglercraft.org/")  
- [https://eaglercraft.me/](https://eaglercraft.me/ "https://eaglercraft.me/")  
- [https://g.lax1dude.net/eaglercraft/](https://g.lax1dude.net/eaglercraft/ "https://g.lax1dude.net/eaglercraft/")  
### There are also multiple community hosted servers, the best way to discover those is to use the [official clients](https://g.deev.is/eaglercraft/)

#### Alternatively, there is a server list[^2] to find servers.

[^2]: Server list is currently being rebuilt, use official client for now

# Creating your own server

There are ***multiple parts*** **to a server**, mainly consisting of a **regular 1.5.2 Bukkit server**, and a **modified version of Bungeecord** called **EaglercraftBungee**, which on top of the regular Bungeecord functionality, it translates WebSocket connections to raw TCP connections which Bukkit can understand.

You may also want to set up your own **client**, allowing you to *control default server listings, resource packs, and an overall faster connection due to less load.*

If you want to use a domain for your server, **a reverse proxy** can be set up to enable extra functionality within EaglercraftBungee. **NGINX** is recommended, and a tutorial is included **[here](#Creating-a-Reverse-Proxy---NGINX)**. **This is optional, and can be skipped by just connecting with the IP.**

### If replit is acceptable, you can use [this](https://replit.com/@ayunami2000/eaglercraft-server) to automatically set up everything for a server, otherwise, look below for instructions

## Creating a server - Bukkit

1. **Check if Java is installed.** You can download it from [https://www.java.com/en/download/](https://www.java.com/en/download/)
2. Download the [stable-download/stable-download.zip](https://github.com/lax1dude/eaglercraft/raw/main/stable-download/stable-download.zip) file from this repository
4. Extract the ZIP file you downloaded to a new folder
5. Open the new folder, go into the `java/bukkit_command` folder
6. In Windows, double-click `run.bat`. It should open a new terminal window  
![run.bat](https://i.gyazo.com/2b0f6b3e5b2e5a5a102c62ea5b6fba3f.png)  
**Some computers may just say 'run' instead of 'run.bat', both are correct**
7. On macOS or Linux, google how to open the terminal and use the `cd` command to navigate to `java/bukkit_command`  
Then, in that folder, run `chmod +x run_unix.sh` and then run `./run_unix.sh`. It should start the same server
8. To add some bukkit plugins, download the plugin's JAR file for CraftBukkit 1.5.2 and place it in `java/bukkit_command/plugins`
(See [https://github.com/lax1dude/eaglercraft-plugins/](https://github.com/lax1dude/eaglercraft-plugins/) to download some supported plugins)

## Creating a server - EaglercraftBungee
1. In the same new folder, go into the `java/bungee_command` folder
2. In Windows, double-click `run.bat`. It should open a second terminal window  
Keep both the first and second terminal window you opened, just minimize them, don't close
3. On macOS or Linux, repeat step 7 in [Creating a Server - Bukkit](#Creating-a-server---Bukkit), but navigate to `java/bungee_command` this time
4. To add some bungee plugins, download the plugin's JAR file and place it in `java/bungee_command/plugins`

There are alot more configurations in bungeecord, but this should set you up

**Your server is now ready.** Visit any client, and go to 'Multiplayer' from the main menu. Select 'Direct Connect', type `127.0.0.1:25565` and press 'Join Server'
**It should allow you to connect, if not, check the two terminal windows for errors**

## Creating a client
1. To install,  upload the contents of `stable-download/web` to a web server.
	- There are *multiple ways of setting up a web server*. **[NGINX](https://nginx.org) is a powerful web server, but alternatives like [Web Server for Chrome](https://chrome.google.com/webstore/detail/web-server-for-chrome/ofhbbkphhbklhfoeikjpcbhemlocgigb?hl=en) may be easier to set up.**
		- A quick crash course on setting up NGINX is provided [here](#Creating-a-Reverse-Proxy---NGINX), **FOLLOW STEPS 1 AND 2 ONLY**, then navigate to `/var/www/html` and upload the contents of `stable-download/web` there.
	- If you had installed NGINX earlier as a reverse proxy, you can also use it to host the client, **follow the steps above ^^^**
		- Make sure that the URL to connect to the client and the server are separate, preferably with a path, like `https://eaglercraft.example.com/server`
	
2. **The 'web' folder will not work if you open it in your browser locally! If you see 'file:///' in the URL you are doing it wrong. You need to upload the folder to an HTTP or HTTPS server and access it over the internet via http:// or https://. The game will not load otherwise, this is not a bug**
3. To modify the list of default servers, modify the `window.eaglercraftOpts` variable in `index.html`. 
4. **A full guide on how to configure `eaglercraftOpts` is coming soon, but it should be fairly intuitive to figure out how to set it up based on what the default values already are when you look in stable-download**
5. **To create a link to your site that automatically joins the server,** add a `?server=` variable to the URL, like (for example): [https://g.deev.is/eaglercraft/?server=127.0.0.1:25565](https://g.deev.is/eaglercraft/?server=127.0.0.1:25565) will automatically join `ws://127.0.0.1:25565/` as soon as the player finishes setting their username and skin

# EaglercraftBungee Configuration

## MOTD
- To change your server's MOTD and icon, edit the `motd1:` tag of the listener config in `java/bungee_command/config.yml`, and replace `server-icon.png` in the folder where the config file is. Use `&` to add color/formatting codes. The server list will downscale your icon to 64x64 pixels
- You can give your MOTD multiple lines, add a `motd2:` to define a second line
- **For an animated MOTD and icon, install EaglerMOTD: [https://github.com/lax1dude/eaglercraft-motd/](https://github.com/lax1dude/eaglercraft-motd/)**

##  Authentication
**To enable the /login and /register commands in EaglercraftBungee, you can edit this portion of config.yml**

```yaml
authservice:
  authfile: auths.db
  register_enabled: true
  ip_limit: 0
  join_messages:
  - '&3Welcome to my &aEaglercraftBungee &3server!'
  login_timeout: 30
  enabled: false
```
- `authfile` Sets the authentication database file, which is **compatible with AuthMe**

- `register_enabled` Turns register command on and off

- `ip_limit` Sets the max number of registrations per IP, 0 = unlimited

- `join_messages` List of messages to show the player when they join

- `login_timeout` Sets how many seconds players have to log in before they are kicked

- `enable` Turns login commands on and off

## Moderation
### Miscellaneous
- **If you use /op on your server, keep in mind that if you "/op LAX1DUDE", a player joining as 'laX1DUDE' or 'LaX1dUdE' or 'lax1dude' will all have /op too. To solve this problem, force all operators to only be able to join with all lowercase ('lax1dude') letters in their usernames by moving 'BitchFilterPlugin.jar" into "java/bukkit_command/plugins" and then register every op username lowercase**

- **To disable voice chat, set `voice_enabled: false` in the bungeecord config.yml**

### Username Bans
- **To ban a username on Eaglercraftbungee, use:** `eag-ban <username>`
- **You can edit bans.txt in your EaglercraftBungee folder, the server automatically reloads the file when it is saved**
- To ban users by regular expression, use: `eag-ban-regex <regex>` with a regular expression to match the username in **lowercase**

### IP Bans

In order for IP Bans to work, a [**a reverse proxy**](#Creating-a-Reverse-Proxy---NGINX) is required, and **[`forward_ip`](#NGINX-Configuration) needs to be configured,** ***otherwise it cannot ban the user's IP***

- **To ban an IP on Eaglercraftbungee, use:** `eag-ban-ip <ip>`, or `eag-ban-ip <name>` to ban the IP of a player automatically
- To ban a range of IP addresses, use slash notation to define a subnet. Example: `eag-ban-ip 192.168.0.0/8`
- To ban users by wildcard (\*) use: `eag-ban-wildcard <text>*` or `eag-ban-wildcard *<text>` or `eag-ban-wildcard *<text>*`

### Client Bans
- **EaglercraftBungee has a built in domain blacklist that updates automatically, you can disable it by setting this in config.yml:**

```yaml
enable_web_origin_blacklist: false
```

- **To block all clients on replit from joining, set this to true in config.yml:**

```yaml
origin_blacklist_block_replit_clients: true
```

- **To block all offline-download clients, set this to true in config.yml:**

```yaml
origin_blacklist_block_offline_download: true
```

- **To block the debug runtime (or other desktop clients), set this to true in config.yml:**

```yaml
origin_blacklist_block_missing_origin_header: true
```
- **To add your own blacklisted domains**, create a file called `origin_blacklist.txt` in your bungeecord directory and put the regular expressions inside, one on each line. There 's also a `domain` command in the console to view a player's domain, and a `block-domain` and `block-domain-name` and `unblock-domain` command to manage the local `origin_blacklist.txt` from the bungee console (if you don't know how to edit a file on your own). The list reloads automatically when changes to the file are detected.

- ### To configure bungee to block connections from all clients except your own, set this option:

```yaml
origin_blacklist_use_simple_whitelist: true
```

Then, add your domain to `origin_blacklist_simple_whitelist` like this:

```yaml
origin_blacklist_simple_whitelist:
- type the name of your client's domain here
```
Then, unless still you want it as an option for your players, disable the offline download so hackers don't use it to bypass the whitelist, as it is not blocked in whitelist mode by default:

```yaml
origin_blacklist_block_offline_download: true
```
## Others

- The server has built in DoS protection, reset it via typing 'eag-ratelimit reset' in the bungee console**

- Rate limiting is possible, but [**a reverse proxy**](#Creating-a-Reverse-Proxy---NGINX) is required, and **[`forward_ip`](#NGINX-Configuration) needs to be configured to use rate limiting,** ***otherwise it will be disabled by default***

```yaml
ratelimit:
  ip:
    enable: true
    period: 90
    limit: 60
    limit_lockout: 80
    lockout_duration: 1200
    exceptions: []
```

- `enable` enable rate limiting

- `period` and `limit` set the number of requests (`limit`) can be made in (`period`) number of seconds

- `limit_lockout` and `lockout_duration` set the number of requests (`limit_lockout`) that can be made in (`period`) seconds before the IP is blocked for `lockout_duration` number of seconds

- `exceptions` a list of IP addresses that should never get rate limited. **Local IPs like 127.0.0.1 and 192.168.\*.\* and such are set as exceptions by default**

- ### Redirecting the client to a new WebSocket

If you would like to signal the client to disconnect from your bungeecord and reconnect to a different bungeecord, configure an entry in the `servers` part of config.yml like this:

```yaml
test:
  redirect: wss://ServerHere/
  restricted: false
```

In this example, sending a player to the server `test`, such as when they enter a portal or type `/server test`, will trigger their client to disconnect from your bungeecord and then automatically reconnect to `wss://ServerHere/` as if it was entered via "Direct Connect"

## Creating a Reverse Proxy - NGINX

Here is a quick crash course of setting up NGINX on Linux, specifically on Debian distributions.

Here are some google searches for other distributions:

- [Windows](https://www.google.com/search?q=set+up+nginx+on+windows)
- [Mac](https://www.google.com/search?q=set+up+nginx+on+mac)
- [Linux - Arch](https://www.google.com/search?q=set+up+nginx+on+arch)
- [Linux - Fedora](https://www.google.com/search?q=set+up+nginx+on+fedora)

1. Open up your terminal, and run  
	``sudo apt update``
  and
	``sudo apt install nginx``
2. Open any web browser, and search for `localhost` in your search bar. You should see something like this:![Welcome to nginx](https://ubuntucommunity.s3.dualstack.us-east-2.amazonaws.com/optimized/2X/7/7504d83a9fe8c09d861b2f7c49e144ac773f0c0d_2_690x288.png)
3. Navigate to NGINX's configuration with `cd /etc/nginx/sites-enabled`.

4. Create a new configuration file with your domain name, for example `nano eaglercraft.example.com`

5. Paste in the following code into the file. Replace `example.com` with your own domain, and `app_server_address` as the `ip:port` of your EaglercraftBungee server you want the URL to connect to.
```
server {
    listen 80;
    listen [::]:80;

    server_name example.com eaglercraft.example.com;
        
    location / {
        proxy_pass app_server_address;
        include proxy_params;
    }
}
```

6. Now, restart NGINX with `sudo service nginx restart` and you should be good to go!

## NGINX Configuration

### To implement the following configuration, add the lines below the `proxy_pass` line.
- **To stop people from using bookmarklets to load a client from a different URL onto your official URL via XXS, add these headers to NGINX:**

```
add_header X-Frame-Options "SAMEORIGIN";
add_header Referrer-Policy "strict-origin";
add_header X-XSS-Protection "1; mode=block";
add_header Content-Security-Policy "default-src 'self' 'unsafe-inline'; img-src 'self' 'unsafe-inline' data: blob:; connect-src 'self' ws: wss:; upgrade-insecure-requests";
```

###### (not fully tested, excuse the scroll bar)

- **To use IP bans and rate limiting, add `proxy_set_header X-Real-IP $remote_addr` to your proxy configuration**

# Others

## Plugin Development

**To develop a plugin, download [stable-download/java/bungee_command/bungee_dist.jar](https://github.com/lax1dude/eaglercraft/blob/main/stable-download/java/bungee_command/bungee-dist.jar) and add it to the Build Path of your Java IDE. Develop the plugin just like a regular BungeeCord plugin, see [EaglerMOTD](https://github.com/lax1dude/eaglercraft-motd/) for an example.**

**Test your plugin by exporting it as a jar and putting it in the '/plugins' directory of EaglercraftBungee and then clicking 'run.bat'**

### New Events:

- **[net.md_5.bungee.api.event.WebsocketMOTDEvent](https://github.com/lax1dude/eaglercraft/blob/main/eaglercraftbungee/src/main/java/net/md_5/bungee/api/event/WebsocketMOTDEvent.java)**: Triggered when a client or website requests the MOTD

- **[net.md_5.bungee.api.event.WebsocketQueryEvent](https://github.com/lax1dude/eaglercraft/blob/main/eaglercraftbungee/src/main/java/net/md_5/bungee/api/event/WebsocketQueryEvent.java)**: Triggered when a client or website requests a query. This happens when a site opens a text WebSocket to a listener and sends a single string `Accept: <query>` packet. Can be used to provide additional custom statistics to server list sites supporting integrated WebSocket queries

**Register event handlers using the standard BungeeCord** `@EventHandler` **annotation in your** `Listener` **class**

## Compiling

To compile for the web, run the gradle 'teavm' compile target to generate the classes.js file.

The LWJGL runtime is no longer supported it is only included for reference

## Creating a resource pack

- To make a custom resource pack for your site, clone this repository and edit the files in [lwjgl-rundir/resources](https://github.com/lax1dude/eaglercraft/tree/main/lwjgl-rundir/resources).
- When you are done, navigate to [epkcompiler/](https://github.com/lax1dude/eaglercraft/tree/main/epkcompiler) and double-click `run.bat`. Wait for the window to say `Press any key to continue...` and close it. Then, go to `../javascript` in the repository and copy `javascript/assets.epk` to the `assets.epk` on your website
- If you're on mac or linux, navigate to the epkcompiler folder via `cd` and run `chmod +x run_unix.sh` and then `./run_unix.sh` to do this, then copy the same `javascript/assets.epk` to the `assets.epk` on your website

## Contributing

All I really have to say is, tabs not spaces, and format the code to be like the eclipse auto format tool on factory settings, but also run-on lines of code long enough to go off the screen and single line if statements and other format violations in that category are welcome if it helps enhance the contrast between the less important code and the more important code in a file. Don't commit changes to `javascript/classes.js` or `javascript/classes_server.js` or `javascript/assets.epk` or anything in `stable-download/`. I'll recompile those myself when I merge the pull request.
