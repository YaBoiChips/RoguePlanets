package yaboichips.rogue_planets.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final String CATEGORY = "key.categories.rogueplanets";

    public static final KeyMapping OPEN_POCKETS = new KeyMapping(
            "key.rogueplanets.open_pockets",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            CATEGORY
    );
}
