# SharePos

A lightweight Spigot plugin that allows players to quickly share their coordinates with clickable chat messages.

## ✨ Features

- **Clickable Coordinates** - Share coordinates that can be copied with one click
- **Flexible Sharing** - Send to specific players or broadcast to everyone
- **Anti-Spam Protection** - 15-second cooldown prevents coordinate spam
- **Operator Privileges** - OPs can share any player's coordinates without cooldowns
- **Multi-World Support** - Works across all worlds with world information
- **Permission System** - Granular control over who can use what features

## 🚀 Quick Start

### Installation

1. Download the latest release from [Releases](../../releases)
2. Place `SharePos-1.1.jar` in your server's `plugins` folder
3. Restart your server
4. Configure permissions (optional)

### Basic Usage

```
/sharepos <player>     - Share coordinates with specific player
/sharepos all          - Share coordinates with all players
/sharepos help         - Show help menu
```

**Operator Commands:**
```
/sharepos <target> all - Share target's coordinates with everyone
```

**Aliases:** `/sp`, `/pos`

## 🔧 Configuration

### Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `sharepos.use` | Basic plugin usage | `true` |
| `sharepos.all` | Share with all players | `op` |

### Example Permission Setup (LuckPerms)
```
/lp group default permission set sharepos.use true
/lp group moderator permission set sharepos.all true
```

## 🛠️ Development

### Building from Source

**Requirements:**
- Java 17+
- Maven 3.6+

**Build Steps:**
```bash
git clone https://github.com/yourusername/SharePos.git
cd SharePos
mvn clean package
```

The compiled JAR will be in the `target/` directory.

### Project Structure
```
src/
├── main/
│   ├── java/ru/sharepos/
│   │   ├── SharePosPlugin.java      # Main plugin class
│   │   ├── SharePosCommand.java     # Command handler
│   │   └── ChatClickListener.java   # Event listener
│   └── resources/
│       └── plugin.yml               # Plugin configuration
```

## 📋 Compatibility

- **Minecraft:** 1.21.x
- **Server Software:** Spigot, Paper, or compatible forks
- **Java:** 17+ (compiled with Java 21)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🐛 Issues & Support

- **Bug Reports:** [GitHub Issues](../../issues)
- **Feature Requests:** [GitHub Issues](../../issues)
- **Discord:** [Your Discord Server](https://discord.gg/yourserver)

## 📊 Statistics

![GitHub release (latest by date)](https://img.shields.io/github/v/release/yourusername/SharePos)
![GitHub downloads](https://img.shields.io/github/downloads/yourusername/SharePos/total)
![GitHub stars](https://img.shields.io/github/stars/yourusername/SharePos)

---

Made with ❤️ by [SHTUKA](https://github.com/yourusername)