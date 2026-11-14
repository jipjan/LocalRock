# What LocalRock still lacks: Saros 20 Neo (a296)

Compared on 2026-11-14 against the official Roborock app (Android 4.60.06, a296 plugin 7052), going
through every page of the official app with its commands captured through local_roborock_server, and
against every feature bit the robot advertises (`new_feature_info_str`, names from the plugin's
NewFeatureStrBit table). See `robot-commands-a296.md` for the commands of everything that is built.

## Beta features list: every advertised bit

### Built
| Bit | Plugin name | Where in LocalRock |
|---|---|---|
| 34, 36, 40, 43, 49, 54, 56, 63, 66, 70, 75, 79, 80, 104, 105, 112, 116, 131, 132, 144 | (various) | Floor, carpet, obstacle and dock pages |
| 37 | VoiceControl | Robot settings → Voice assistant (`get/set_voice_service_switch {"data"}`) |
| 50 | PetSnapshot | Obstacles → Pet Snaps (`set_camera_status`, bit 4096) |
| 121 | ProductExperienceImprovement | Robot settings → Share usage data / Share voice data |
| 140 | SupportMultipleDNDTimePeriods | Do Not Disturb, up to 3 periods |
| – | (no bit) | Obstacles → Fill light (camera bit 2) and I have pets (camera bit 32) |
| – | (no bit) | Robot settings → Voice pack (`dnld_install_sound`, packs served by the local server) |
| 100 | CollectDustCountShow | History → "Bin emptied" total (`get_clean_summary` → `dust_collection_count`) |
| 71 | MapEraser | Map editor → Eraser (`app_set_beautify_blocks`, map block 47) |
| 108 | CleanEfficiency | History → m²/min per clean, with obstacles avoided, mop washes and bin emptied from the record |

### Handled on another page (not listed as missing)
35/112 cleaning fluid · 41 water temperature · 42 cleaning direction · 47/51 per-room settings ·
53 auto-empty · 83 drying at a set time · 93/102/114 vacuum first, then mop · 107/109 wash modes ·
113 adaptive lift · 118 collision mode · 55/106 SmartPlan, water slide · 32/33/73 camera (remote
viewing; needs the server's TURN relay).

### Nothing to switch (not listed)
The official app has no setting for these either: 38 NewEndpoint, 52 NewAIRecognition, 57
NoNeedCarpetPressSet, 62 MinBattery15ToCleanTask, 64 ObjectDetectCheck, 67 Matter (needs Roborock's
cloud), 77 MaxZoneOpened, 86 SupportMainBrushUpDown, 87 EggDanceMode (demo), 96
SupportGetParticularStatus, 98 SyncServerName, 99 ShouldShowArmOverLoad, 101 supportApiAppStopGrasp,
110 DualBandWiFi, 119 CarpetShapeType, 143 CancelOneTimeSweepThenMop, 148 SupportWashPhasePumping.

### Still missing (listed on the Beta page)
| Bit | Plugin name | What it is | Commands (plugin) | Effort |
|---|---|---|---|---|
| 85 | MidwayBackToDock | Returns to the dock mid-clean to wash/empty; shown in the clean's status | not found in the capture | needs a capture during a clean |
| 122 | FloorPlanMap | Floor-plan style map view | map rendering | large |
| 48, 59, 68, 74, 95, 103, 123, 126, 127 | (not in the plugin's table) | unknown | – | – |

## Official app compared page by page

| Official app (Dutch label) | LocalRock |
|---|---|
| Geschiedenis (history) | Built, with efficiency, obstacles avoided, mop washes and bin emptied. Missing: the map of a past clean (`get_clean_record_map`) and its timeline (bits 76/91). |
| Planningen (schedules) | Built (`set_timer`/`upd_timer`). |
| Onderhoud (maintenance) | Built. |
| Tapijtreinigingsinstelling (carpet) | Built, all options. |
| Instelling voor vloerreiniging (floor) | Built, all options. |
| Instellingen dockingstation (dock) | Built, all options. |
| Reactief obstakelvermijding (obstacles) | Built except **Obstacle photos** (camera bit not identified: turning it on needs a privacy consent in the official app, so it was not captured) and the photos themselves on the map. |
| Huisdieren (pets): Ik heb huisdieren, Pet Snaps | Built. |
| Huisdieren: Zoek het huisdier (find the pet) | Missing: `app_start_pet_patrol`, a camera patrol. |
| Huisdieren: Huisdierenalbum (pet album) | Missing: `get_pet_album`, `get_pet_album_photos`, photos are encrypted like obstacle photos. |
| Huisdieren: Reinigingsinstellingen (pet cleaning settings) | Not opened yet; probably pet supplies deep clean (built). |
| Kaarten beheren (maps): save, multiple floors, backup | Built (map list, switch, backup/restore, rename). |
| Kaart bewerken: splitsen, combineren, naam, verboden zones, type vloer, tapijt, volgorde | Built. |
| Kaart bewerken: **Drempel** (door sills) | Built (`app_set_smart_door_sill`). The a296 takes LocalRock's shorter form without `attrs`/`side_heights` (tested). |
| Kaart bewerken: **Meubels** (furniture) | Built: the map editor's Furniture tool with all 20 types (`save_furnitures`, map block 25). |
| Kaart bewerken: **Gordijn** (curtains) | Built: the map editor's Curtain tool (`app_set_curtains`, map block 59). |
| Kaart bewerken: **Onder meubels** (under furniture) | Built: the map editor's Under furniture tool (`app_set_low_space_zones`, map block 52). |
| Kaart bewerken: tapijt (rectangular and round) | Built (`set_carpet_area` with flags). Ignoring a detected carpet (`set_ignore_carpet_zone`) is not in this robot's editor. |
| Stemassistent: switch | Built (off; turning it on lets the robot process your voice). |
| Stemassistent: Taal (voice pack) | Built, served by the local server. |
| Stemassistent: Volume | Built (robot volume). `set_voice_chat_volume {"volume"}` is not this: in the plugin it is the talk-back volume of the remote-viewing screen, so it comes with remote viewing. |
| Stemassistent: Chatgeschiedenis (chat history) | Missing: `get_voice_history`, `get_voice_history_summary`, `delete_all_voice_history`. |
| Kijken op afstand (remote viewing) | Built in LocalRock (needs the server's TURN relay and the robot's camera password). |
| Verbinding maken met Matter | Not possible locally (needs Roborock's cloud and a Matter hub). |
| Meer opties: DND, oplaadmodus, knopverlichting, kinderslot, offline kaart | Built (charging mode = off-peak charging on or off). |
| Pin n Go, Afstandsbediening | Built. |
| Gebruiksaanwijzing, Productinformatie, Apparaat delen | Not needed locally (manual link, serial number, sharing through Roborock's account). |
| Firmware bijwerken | LocalRock checks; installing needs Roborock's update server. |
| Main screen: Volledig, Kamer, Zones, Routine, Stofzuigen dan dweilen | Built (routines as scenes). |

## Things the official app sends that LocalRock deliberately does not
`set_user_privacy`, `enable_log_upload [9,3]`, `set_fds_endpoint` (a Roborock log-upload host). The
official app sends the last two every time it opens the robot, even through the local server.

## Not tested on this robot because it lacks them
Intensive corner mopping (`set_corner_clean_mode`) and the distance-based custom water flow
(`set_water_box_distance_off`) are built behind their feature bits; this a296 has neither.

## Remote viewing (Kijken op afstand): paused 2026-11-14, to be finished

Done:
- Server (bezemer-wit-server): TURN in provided mode (host api-roborock.stomp.cloud, UDP 3478 and
  49160–49179 published and forwarded on the router; a STUN request from outside answers), and the
  14-character camera domain `rr.stomp.cloud` in Caddy with a valid certificate. `POST
  https://rr.stomp.cloud/iot.roborock.com/fwapi/createca` returns the relay details.
- LocalRock pairing has a Camera domain field; the robot was re-paired with `rr.stomp.cloud` and
  fetched createca from it (20:02 UTC).
- Captured when the official app began activating: `set_user_privacy {"agree":1,"owner":1}`,
  `set_camera_status [4515]` (419 + 4096) and 15 s later `[419]` again. So bit 4096 may belong to
  activation rather than (only) Pet Snaps; check before relying on the Pet Snaps switch.

Still to do (needs the robot to beep/speak and the power button pressed, so only when it may make noise):
- Finish the activation in the official app while recording: the remote-view pattern
  (`set_homesec_password`), the two-key `set_camera_status` request and the power-button press.
- Build activation and the live view into LocalRock (CameraLiveSession already does the WebRTC part)
  and test a stream through the relay.

## Other things captured, not built yet

- Voice chat history: `get_voice_history_summary []` → `{"min_id":0,"max_id":0,"count":0}`, then
  `get_voice_history {"min_id","max_id"}`. This robot's history is empty (voice assistant off), so the
  entry format is unknown; build once there is a conversation to read.
- Pet album: `get_pet_album {from_task, from_photo, max_task_count, max_photo_count, photo_type,
  end_point, security}` and `get_pet_album_photos` (encrypted like obstacle photos). Empty here
  (Pet Snaps off), so not built.

## Built after the first list

- Past clean: History → tap a clean: its map with the path (`get_clean_record_map {"start_time"}`,
  answered through the map channel) and a timeline of state changes with battery (`get_clean_detail`
  event 11). Other event types (rooms, washes, obstacles) are not decoded yet.
