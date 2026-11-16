package com.kodraliu.localrock.ui.vacuum

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KingBed
import androidx.compose.material.icons.filled.Portrait
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SingleBed
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material.icons.filled.Yard
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * The icon for a room type (`get_room_mapping`'s tag), chosen to look like the official app's
 * room badge for that type. The plugin has artwork for types 1–3, 6–10 and 12–15; others get none.
 */
internal fun roomTagIcon(tag: Int?): ImageVector? = when (tag) {
    1 -> Icons.Default.Bed               // bedroom with a lamp
    2 -> Icons.Default.KingBed           // second bedroom
    3 -> Icons.Default.SingleBed         // children's room
    6 -> Icons.Default.Weekend           // living room (sofa)
    7 -> Icons.Default.Yard              // balcony (plant)
    8 -> Icons.Default.Portrait          // framed picture
    9 -> Icons.AutoMirrored.Filled.MenuBook // study (open book)
    10 -> Icons.Default.DoorFront        // hallway
    12 -> Icons.Default.Home             // other room
    13 -> Icons.Default.Restaurant       // dining room
    14 -> Icons.Default.SoupKitchen      // kitchen (steaming bowl)
    15 -> Icons.Default.Wc               // toilet
    else -> null
}
