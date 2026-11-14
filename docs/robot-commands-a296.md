# Robot commands: Saros 20 Neo (roborock.vacuum.a296)

The settings the official Roborock app can change on this robot, with the exact commands and
parameters it sends, so LocalRock can offer all of them.

**Sources**
- **Captured**: commands the official Roborock app (Android 4.60.06, plugin 7052) sent to a
  Saros 20 Neo on firmware 02.32.54 through local_roborock_server, on 2026-11-14. Each setting
  was changed in the app and the command read from the server's MQTT log. Highest confidence.
- **Plugin**: names, parameter shapes and value meanings read from the official a296 device
  plugin (version 7052, product id 257), with the owner's permission. No plugin code is copied
  here. Used where the capture did not cover a setting.

Parameters are shown exactly as sent. Most are a bare JSON object, not wrapped in an array.

## How the official app behaves

- **The carpet page saves when you leave it.** Changing options there sends nothing; leaving the
  page sends every carpet command at once (mode, cross, priority, long-haired, boost, deep clean,
  wire detection).
- **The carpet mode resets the carpet sub-options.** Choosing "avoid", "cross" or "vacuum and mop"
  and coming back to "vacuum only" saved *Carpets first*, *Deep clean* and *Wire detection* as off.
- **One switch can be several commands.** *AI environment recognition* sends three commands
  (ground material, furniture, room).
- **Mop wash mode and water temperature go together.** Changing the wash mode also sends a matching
  `set_wash_water_temperature`.
- **Some changes need consent or confirmation**: obstacle photos (privacy consent), switching off
  reactive avoidance (it also switches off obstacle photos and *I have pets*), switching off the fill
  light (warns that object recognition gets worse).
- **On first login the app also sends**: `set_user_privacy {"agree":1,"owner":1}`,
  `enable_log_upload [9,3]`, `set_fds_endpoint [...]` (a Roborock log-upload host),
  `set_app_timezone ["Europe/Amsterdam",2]`, `set_voice_service_switch {"data":0}`, and the DND timer.
  LocalRock must not send the privacy, log-upload or endpoint ones.

## Cleaning

| Setting | Read | Write | Values | Source |
|---|---|---|---|---|
| Vacuum then mop | `get_status` → `seq_type`, `repeat` | `app_set_clean_sequence_type {"type":1,"repeat":1,"fan_power":101,"mop_mode":300,"water_box_mode":235}` | type 1 = vacuum then mop, 0 = together; current suction/mop/water are sent along | Captured |
| Room order | `get_clean_sequence` | `set_clean_sequence {"data":[3,2,1],"need_retry":1}` | room ids in order; the app had to retry once (`retry_request`) | Captured |
| Re-mop dirty spots | `get_offline_map_status` → `common_switch_status` bit 4 (16) | `app_set_dirty_replenish_clean_status {"status":0/1}` | 0/1; not sent while cleaning | Captured |
| Cleaning direction | `app_get_robot_setting {"name":"clean_direction_status"}` | `app_set_robot_setting {"name":"clean_direction_status","status":n}` | 0 smart, 1 along floor direction, 2 crosswise | Captured |
| Low-area access (under furniture) | robot setting `restrict_low_area_access_status` | `app_set_robot_setting {...,"status":n}` | 2 off, 1 safe clearance, 0 extreme clearance | Captured |
| Arm for high door sills / carpets | robot setting `enable_sway_arm_for_high_door_sill_carpet` | `app_set_robot_setting {...,"status":0/1}` | 0/1 | Captured |
| Gap deep clean | `get_gap_deep_clean_status` | `set_gap_deep_clean_status {"status":0/1}` | already in LocalRock | Captured |
| Dirty object detection | `get_dirty_object_detect_status` | `set_dirty_object_detect_status {"status":0/1}` | already in LocalRock | Captured |
| Pet supplies deep clean | `get_pet_supplies_deep_clean_status` | `set_pet_supplies_deep_clean_status {"status":0/1}` | already in LocalRock | Captured |
| Re-clean areas blocked by people/pets | robot setting `dynamic_obstacle_replenish_status` | `app_set_robot_setting {...,"status":0/1}` | 0/1 | Captured |
| Auto-detach mops | robot setting `enable_remove_mop_status` | `app_set_robot_setting {...,"status":0/1}` | 0/1 | Captured |
| Repeat count | `get_status` → `repeat` | `set_clean_repeat_times {"repeat":n}` | passes; the official app sends it as soon as the count changes. Checked on the robot | Plugin |
| Intensive corner mopping | `get_status` → `corner_clean_mode` | `set_corner_clean_mode {"status":0/1}` | only when bit 31 of the last 8 hex digits of `new_feature_info_str` is set; **not** on this a296 | Plugin |
| Water flow (slide) | `get_status` → `water_box_mode` | `set_water_box_custom_mode [n]` | robots with new feature bit 106 (this a296): any of 221–250, the official slider; marks 225/235/245, "extra high" from 248. Checked on the robot | Plugin |
| Custom water flow (no slide) | `get_status` → `distance_off` | `set_water_box_custom_mode 207`, then `set_water_box_distance_off {"distance_off":n}` | only robots without the slide and with bit 31 of `new_feature_info` (number); range 60–205 (85–230 with water modes), shown reversed | Plugin |
| Uncleaned-area reminder | robot setting `enable_omission_area_clean_status` | `app_set_robot_setting` | 0/1 | Plugin |
| LiDAR lowering | robot setting `lds_lifting_switch` | `app_set_robot_setting` | 0/1 | Plugin |
| Skip current area | – | `app_skip_current_cleaning_area {"source":2}` | | Plugin |

## Carpet (saved together when leaving the page)

| App option | Commands |
|---|---|
| Vacuum only (don't wet carpet) | `set_carpet_clean_mode {"carpet_clean_mode":3}` + `app_set_cross_carpet_cleaning_status {"status":0}` |
| Avoid carpet | `set_carpet_clean_mode {"carpet_clean_mode":0}` + cross `0` |
| Cross carpet | `app_set_cross_carpet_cleaning_status {"status":1}` |
| Vacuum and mop | `set_carpet_clean_mode {"carpet_clean_mode":2}` + cross `0` |
| Carpets first | `app_set_priority_carpet_cleaning_status {"status":0/1}`; read `get_offline_map_status` → `common_switch_status` bit 3 |
| Adaptive lift (high-pile, needs carpets first) | `app_set_robot_setting {"name":"long_haired_carpet_clean_status","status":0/1}` |
| Carpet boost | `set_carpet_mode [{"enable":1}]` (only `enable`; the read answers more fields) |
| Deep carpet clean | `app_set_carpet_deep_clean_status {"status":0/1}` |
| Avoid wires at carpet edges | `app_set_robot_setting {"name":"nearbycarpet_detecting_wire_status","status":0/1}` |

Reads on opening the page: `get_carpet_clean_mode`, `get_carpet_mode`, `app_get_carpet_deep_clean_status`,
robot settings `long_haired_carpet_clean_status` and `nearbycarpet_detecting_wire_status`.

## Obstacles and camera

| Setting | Write | Values | Source |
|---|---|---|---|
| Collision mode | `set_collision_avoid_status {"status":n}` | **1 large coverage, 2 less collision** (LocalRock's 0/1 switch is wrong for this robot) | Captured |
| AI environment recognition | `set_identify_ground_material_status {"status":0/1}` + `set_identify_furniture_status {"status":0/1}` + `app_set_robot_setting {"name":"identify_room_status","status":0/1}` | one switch, three commands | Captured |
| Camera functions | `set_camera_status [n]` | bitmask; 387 = 256+128+2+1. Value 2 = fill light automatic (385 ↔ 387). 32 and 4096 = pet camera options. Others unknown | Captured |
| Image watermark | `switch_water_mark {"waterMark":"OFF"}` | | Captured |

## Dock

| Setting | Write | Values | Source |
|---|---|---|---|
| Mop wash interval | `set_smart_wash_params {"smart_wash":n,"wash_interval":s}` | smart_wash 0 = by time (600–1500 s), 1 = per room, 2 = smart | Captured |
| AI mop washing | `app_set_robot_setting {"name":"smart_wash_status","status":0/1}` | | Captured |
| Wash mode | `set_wash_towel_mode {"wash_mode":n}` | 0, 1, 2, 8 (8 not in LocalRock; probably soak) | Captured |
| Wash water temperature | `set_wash_water_temperature {"values":n}` | 0, 1, 2 | Captured |
| Cleaning fluid | `set_auto_delivery_cleaning_fluid {"status":n}` | **0, 1, 3** (not just on/off) | Captured |
| Auto empty | `set_dust_collection_switch_status {"status":n}` | **0, 1, 2** (not just on/off) | Captured |
| Empty intensity | `set_dust_collection_mode {"mode":n}` | 0, 1, 2, 4 | Captured |
| Drying | `app_set_dryer_setting {"status":n,"on":{"dry_time":s},"cron":"M H * * *"}` | status 0 off, 1 automatic, 2 scheduled (`cron` set); dry_time 7200/10800/14400 | Captured |
| Dock cooling fan | `app_switch_dock_cool_fan {"status":n}` | | Plugin |

## Robot

| Setting | Write | Values | Source |
|---|---|---|---|
| Child lock | `set_child_lock_status {"lock_status":0/1}` | already in LocalRock | Captured |
| Do not disturb | `set_multi_dnd_timer [{"actions":{"dry":0,"dust":0,"led":1,"resume":1,"vol":1},"dnd":[22,0,8,0],"enabled":1}, ...]`; read `get_multi_dnd_timer` | bit 140: up to 3 periods (plugin), each with its own `enabled` and `actions`, no two with the same times. The whole list is sent each time; the robot keeps it sorted by start time and drops periods left out | Captured, checked |
| Voice assistant | `set_voice_service_switch {"data":0/1}` | | Captured |
| Map save reminder | `save_map_remind {"map_index":0,"remind":1}` | | Captured |
| Offline map | `set_offline_map_status {"status":0/1}`; read `get_offline_map_status` → `common_switch_status` bit 0 | | Plugin |
| Data-sharing consent | robot settings `product_experience_status`, `voice_experience_status` | 0/1 | Plugin |

## Do not send

- `set_map_beautification_status`: a debug bitmask of map layers, not a user setting (65535 = all on).
- Debug and calibration: `set_wash_debug_params`, `set_mop_reverse_pwm_values`, `set_debug_voxel_map`,
  `upload_data_for_debug_mode`, `set_lab_status`, `set_fan_motor_work_timeout`.
- Endpoints, config, firmware: `app_set_dynamic_config`, `set_fds_endpoint`, OTA commands,
  `enable_log_upload`, `set_user_privacy`.
- Factory, test and demo: `app_amethyst_self_check`, `app_amethyst_drain_all_water`, the `test_*` voice
  commands (except `test_sound_volume`), `set_ces_action`, `app_exhibition_*`.
- Destructive: `reset_map`, `del_map`, backup-file commands, `app_delete_wifi`, `update_dock`.
- Hardware this robot lacks: `app_arm_*`, `app_set_clean_mop_type`, `app_set_clean_mop_power`.

## The option bitmask

`get_offline_map_status` answers `{"common_switch_status": n}`, one bitmask of options: bit 0 offline
map, bit 1 cleaning-fluid module, bit 3 carpets first, bit 4 re-mop dirty spots, bit 5 cross carpets.
An a296 answered 10 with carpets first on. Its `get_status` has no `switch_status` field.

## Labels the official app shows (Dutch app, a296)

- Cleaning fluid dosing: 0 None, 1 Standard, 3 High volume.
- Auto-empty frequency (`set_dust_collection_switch_status`): 0 Off, 1 Balanced, 2 High (also returns
  to empty during a clean).
- Empty mode (`set_dust_collection_mode`): Smart, Light, Balanced, Maximum (0, 1, 2, 4).
- Wash mode: "Smart" is `smart_wash_status` 1; "Custom" is `wash_mode` plus the water temperature.

## Status in LocalRock (branch feat/volume-slider)

Built and checked on an a296 against the official app's values and commands:
- Every robot-setting switch and picker above (floor, carpet, obstacle, dock), read and written
  with the same commands and parameters as the official app.
- Collision mode picker (1/2), cleaning fluid and auto-empty frequency pickers.
- Carpets first, re-mop dirty spots and cross carpets, from the option bitmask.
- Carpet mode with the official choices (0 avoid, 3 vacuum only, 2 vacuum and mop).
- Vacuum then mop (`app_set_clean_sequence_type`, byte for byte as captured).
- Mop drying Off / After cleaning / At a set time (`status` 0/1/2 with a daily cron).
- Smart mop washing as `smart_wash_status`; wash mode 10 is no longer offered on AI-wash robots.
- Cleaning count read from and saved on the robot (`set_clean_repeat_times`).
- The 30-step water slider (221–250) on water slide robots.
- Quiet hours with `set_multi_dnd_timer`, keeping the robot's `actions`; up to 3 periods, each
  switched, changed and deleted on its own. The a296 refuses LocalRock's
  old `set_dnd_timer` object (`get_dnd_timer` and `close_dnd_timer` do work), and that refusal used to
  be swallowed.

Built from the plugin, gated by the robot's flags, but not checked on a robot (this a296 has neither):
- Intensive corner mopping: a switch on the Vac & Mop and Mop tabs (`set_corner_clean_mode`).
- Custom water flow as a distance: a Custom choice with a slider (`set_water_box_custom_mode [207]`,
  then `set_water_box_distance_off`), range 60–205. Spin-mop models may allow 85–230; LocalRock keeps
  to the base range.

Not built: the camera bitmask (fill light is value 2), and linking
wash mode to water temperature.

## Map editor (captured 2026-11-14, each drawn in the official app and deleted again)

Every edit is wrapped in `start_edit_map []` … `end_edit_map []`. Coordinates are map millimetres,
four corners per shape (x1,y1,x2,y2,x3,y3,x4,y4).

| Tool (Dutch label) | Command | Notes |
|---|---|---|
| Gordijn (curtain) | `app_set_curtains {"map_index":0,"zones":[[x1,y1,…,x4,y4,0,0], …]}` | the whole list each time; `"zones":[]` removes all. A 2.3 m × 0.1 m strip by default |
| Drempel (door sill) | `app_set_smart_door_sill {"attrs":[10],"map_index":0,"side_heights":[[0,0,0,0]],"sides_infos":[[0,0,0,0]],"zones":[[2,x1,y1,…,y4]]}` | whole list; all lists empty removes all. LocalRock sends the S8 form without `attrs`/`side_heights`; the a296 accepts it and the sill shows in map block 28 (tested 2026-11-14). The plugin fills `attrs`/`side_heights` from the robot's own sill data (block 31), which is empty here |
| Meubels (furniture) | `save_furnitures {"data":[[op,id,x1,y1,…,y4,type,subtype,1], …],"map_flag":0}` | changes only: op 1 = add (id -1), op 0 = delete (`[0,id]`). Types below |
| Map eraser (bit 71; not opened in the official app) | `app_set_beautify_blocks {"map_index":0,"data":[[type,x1,y1,…,y4], …]}` | the full list each time (type 0 = drawn by the user; the robot's suggestions keep their type); `"data":[]` removes them all. The robot itself blanks the map pixels inside, and puts them back when the area is removed. Tested by LocalRock 2026-11-14 |
| Onder meubels (under furniture) | `app_set_low_space_zones {"map_index":0,"zone_data":[[op,x1,y1,…,y4], …]}` | op 0 = add, op 2 = delete that zone; the map shows the change after a reload |
| Tapijt → rond (round carpet) | `set_carpet_area {"flags":[…],"map_index":0,"type":1,"zone_data":[[…], …]}` | whole list with one flag per carpet; round carpet `0x04330000` (round bit `0x04000000`); detected carpets keep their own flags (e.g. `0x331c7e`). Already built in LocalRock |

Furniture types (type, subtype): TV cabinet 43,0 · bed double 45,2 · bed single 45,1 · sofa single
46,1 · two-seat 46,2 · multi-seat 46,3 · modular 46,5 · dining set 47,0 · coffee table 48,1 · square
table 48,2 · shoe rack 49,0 · nightstand 50,0 · wardrobe 51,0 · litter box 52,0 · closed litter box
53,0 · pet cage 54,0 · pet basket 55,0 · pet bowls 56,0 · floor mirror 57,0 · cat tree 58,0.

### Where the map keeps them (legacy `rr` map, a296)

| Block | Holds | Entry |
|---|---|---|
| 25 | furniture | 23 bytes: 8 × u16 corners, u16 10000, u8 type, u8 subtype, u8 1, u8 id, u8 1 |
| 52 | under-furniture zones | 20 bytes: u32 kind (0 drawn by the user, 1 found by the robot, 2 erased by the user), 8 × u16 corners |
| 47 | map eraser areas | 20 bytes: u32 type, 8 × u16 corners |
| 55 | floor plan (bit 122) | per room: u16 room id, u16 point count, u16 point size (6), then per point u16 x, u16 y (mm) and u16 edge flag (1 along a wall, other values where it meets another room). The official app fills each room as this polygon and hides the pixel walls inside it; floor with room 0 inside shows grey |
| 59 | curtains | 20 bytes: 8 × u16 corners, 2 × u16 (0, 0) |
| 39 | carpets with flags | the a296 keeps its carpets here only; block 22 stays empty |

The robot keeps its own copy (kind 1) of a zone the user draws, so removing a zone needs an erase
mark (kind 2) over it, as the official app sends. Erase marks stay in the list.

## Past clean timeline (`get_clean_detail`)

Events `{"t": epoch, "i": type, "e": "<JSON>"}`, matched against the official app's timeline of the
same clean (a296, 2026-11-14):

| Type | Official app shows | Fields |
|---|---|---|
| 25 | Start (full) clean | 251 source (2 app), 252 clean type (1 full), 3 suction, 4 water, 5 route, 6 passes |
| 42 | Cleaning <room> / re-clean areas blocked by people or pets | 2 room id, 421 kind (3 = re-clean blocked areas), 3/4/5/6 as above |
| 52 | Mop wash | 521 wash mode (10 smart), 522 water temperature (0 room, 1 warm, 2 hot) |
| 53 | Resume (after a wash) | |
| 21 | Settings changed by hand | 211 what (1 water flow), 212/213 old/new |
| 12 | Clean finished | 121 |
| 23 | Auto-empty | 231 frequency (1 balanced, 2 high) |
| 11 | (not shown) state change | 111 from, 112 to, 1 battery |

Settings fields in events 25 and 42, as the official app's room card shows them (icon + label):
3 suction (1 Quiet, 2 Standard, 3 Strong, 4 MAX, 5 MAX+; quiet uses a moon icon, the rest a fan),
4 water (0 off, 1 Low, 2 Medium, 3 High; water drop), 5 route (0 Standard), 6 passes (×n). With
suction 0 the card says "Mop only", with water 0 "Vacuum only", otherwise "Vacuum and mop".

Not matched to anything shown: 13, 22, 24, 41 (room entered), 43 (room left), 47 (room queue), 51, 54, 61, 62.
