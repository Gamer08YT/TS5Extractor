# TS5Extractor
Extract your user data from Matrix from the TeamSpeak database to be able to use it in Elements, for example.

For testing purposes only, it is not recommended to use TeamSpeak Chat with other services. Since TeamSpeak has not yet integrated or prohibited all matrix functions that cause errors.

Download current Build -> [Download Here](https://space.byte-store.de/external/bytestore/download/software/extractor/TeamSpeakExtraction.zip)<br>
Download current Beta Build -> [Download Here](https://space.byte-store.de/external/bytestore/download/software/extractor/TeamSpeakExtraction-1.10.zip)

Beta features Function to extract Data from the (new TS5 Servers) Matrix Implementation. 

![image](https://user-images.githubusercontent.com/31771657/163729102-b670fb86-ace7-4c72-b8e6-1f6016b049c6.png)

## Select your Settings Database:

Windows: %appdata%/TeamSpeak/settings.db

![aaa](https://user-images.githubusercontent.com/31771657/163729183-81134b23-9cbc-4a5d-85bb-b5be9b65a493.png)

## Use the Credentials in Elements.io

To use your Credentials you need to change your User Agent to ``Go-http-client/2.0``.

![image](https://user-images.githubusercontent.com/31771657/163794657-50a88444-0d4d-4980-a40a-50507c6286d4.png)

![image](https://user-images.githubusercontent.com/31771657/163794746-f3e2460e-6332-4f43-9fef-e0c13f0b2d14.png)

When you like to use a Mobile App or the Elements Desktop App, you can create a Reverse Proxy with NGINX to do that automatically.

```nginx
location ^~ / {
	proxy_ssl_server_name on;
	#proxy_ssl_name "sni.cloudflaressl.com";
	proxy_pass https://chat.teamspeak.com;
	proxy_set_header Host chat.teamspeak.com;
	#proxy_pass_request_headers on;
	proxy_intercept_errors on;
	proxy_set_header User-Agent "Go-http-client/2.0";
}
```

The current Version is fully functional, but it's missing some Messaging and Logging Options.

## Login into TS5 Server Matrix Chat

### 1. Select Homebase
Select any Homebase you want.

![Tool opened with list of Homebases](https://files.dunkelmann.eu/other/TS5ExtractorImg/bild1.png)

### 2. Copy Access Token
![Selected Homebase with credentials](https://files.dunkelmann.eu/other/TS5ExtractorImg/bild2.png)

### 3. Login using Element

Open Element Web or Element Desktop. 
Then, press <kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>I</kbd> to open JavaScript-Console.

Execute 
```js
mxLoginWithAccessToken("https://<your-home-server>", "syt_your_token");
```
in order to log in.

![Element with Dev Console](https://files.dunkelmann.eu/other/TS5ExtractorImg/bild3.png)

### 4. You're in!

![Logged into Element](https://files.dunkelmann.eu/other/TS5ExtractorImg/bild4.png)
