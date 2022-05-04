# RegenAssist

## About
RegenAssist is an add-on to [Multiverse](https://dev.bukkit.org/projects/multiverse-core) that makes it possible to regenerate Multiverse worlds easily with one command and automatically restore any Multiverse portals in the process. This is particularly useful for world(s) you would like to reset regularly, such as resourceworlds. 

Because RegenAssist lets you configure which world(s) should be on the candidate-list for regeneration, you can allow anyone with the regenassist.regen permission to regenerate _only_ the world(s) on the list. To use the regen command, you don't need to know a lot about Multiverse either, because RegenAssist's tab-complete suggestions will walk you through the process.

## First Time Use
Before using RegenAssist, you'll need to do the following:
1. Add world(s) of your choice to the worldlist in the config 
2. Set permissions:
   - regenassist.regen (to be able to use the regen command)
   - regenassist.reload (to be able to reload the config)
   - regenassist.* (for all permissions)

## Features
* Use a central regen command that shows tab-complete suggestions for:
  - Worldname 
  - What seed to use for the regeneration (same seed/random seed/a seed you supply)
  - An optional flag to reset the world's gamerules (they will be kept the same on default)
* See when a world was last regenerated before continuing with the regen
* Support regenerating from console as well as in-game
* Automatically teleport players to spawn when their login location is in a world that has been regenerated since they logged out (to prevent deaths due to terrain changes around the player)
* If there is a Multiverse portal in your world, RegenAssist can:
  - Find a safe location to create a new portal structure (should work in all world types, including the Nether!)
  - Link the existing Multiverse portal to this structure
  - Set the world's spawn on a small platform next to the portal

## Additional Options
**General:** 
  - Choose which world(s) to allow regeneration for
  - Choose which world to teleport players to if their login location is unsafe

**Portal:**
  - Enable/disable Multiverse portal restoring 
  - Location choice: either [0, 0] or vanilla spawn
  - Block choice: the platform, frame and inside blocks for the portal can be changed per world type (so you can have a different default portal-look in a normal world, Nether or End)
![Overworld_Portal](resources/screenshots/Overworld_Portal.png)

## Author Info
I am new to programming, and this is the first official plugin that I've made. I greatly enjoyed making it, and if you have any questions, remarks, or suggestions, please let me know! You can find me on [GitHub](https://github.com/Artemis-the-gr8) or reach me by email: artemis.the.gr8@gmail.com

## Licence
RegenAssist is licenced under the MIT licence. Please see [LICENCE](LICENSE) for more information.
