## About ShareWiFi

The ShareWiFi application allows to share Wi-Fi networks with all your devices, your friends, or with everyone. The networks are automatically registered on your device. Hence, your Android device will connect with the shared Wi-Fi networks automatically. This reduces the usage of mobile data or enables internet connection in other countries. If a network provides no internet, ShareWiFi will automatically disconnect.

## How it works (vision)

1. Create user groups inside ShareWiFi, e.g. _family_ and _friends_. Users that are not already registered at ShareWiFi, will be invited by mail. Creating user groups is an optional step but a requirement for the group share.

2. Connect with a new Wi-Fi network. ShareWiFi then will ask you whether you want to share this Wi-Fi. Alternatively you can also share registered networks.

3. Once you want to share a Wi-Fi network, select from three share options: 
  * Private Share: Share this network with all devices where you are logged in.
  * Group Share: Share this network with one or more user groups, e.g. the groups _family_ and _friends_.
  * Public Share: Share this network with everyone on the world.

  In the background, ShareWiFi will encrypt the network information and submit it to our server. All information is never stored as plain text for security reasons. Android devices will frequently (if connected via Wi-Fi) sync the Wi-Fi networks. The sync can also be triggered manually. Based on the current location ShareWiFi will register relevant Wi-Fi networks at the Android system. As you can see, ShareWiFi relies on the default Android system to establish network connections.

4. Turn off sharing if you like.

  In the background, ShareWiFi will update the network information database. Don't worry about devices that already know your shared Wi-Fi information. ShareWiFi validates the latest connection and will disconnect automatically if this network is expired.

## Development plan

The development has just started. I plan to create four milestone releases:

* Milestone 1: Respresentation of Wi-Fi information. Connect/delete a Wi-Fi network. Share Wi-Fi network using the Android's build-in share options. Persistent Wi-Fi information on the local device.

* Milestone 2: Upload to server. Group and user management. Auto-sync inside Wi-Fi's and manual sync with server. Validation of Wi-Fi connects. Location-based add/remove of Wi-Fi networks.

* Milestone 3: Encryption. Key management.

* Milestone 4: User settings: Enable/disable auto-connect with non-encrypted Wi-Fi's.
