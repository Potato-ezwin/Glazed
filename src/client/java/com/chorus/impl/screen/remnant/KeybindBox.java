package com.chorus.impl.screen.remnant;

import chorus0.Chorus;
import com.chorus.api.module.setting.implement.KeybindSetting;
import com.chorus.api.system.render.Render2DEngine;
import com.chorus.api.system.render.font.FontAtlas;
import com.chorus.common.QuickImports;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class KeybindBox implements QuickImports {
    private final KeybindSetting keySetting;
    private float x, y, width, height;
    private boolean isListening = false;

    private boolean isHovered = false;

    public KeybindBox(KeybindSetting keySetting, float x, float y, float width, float height) {
        this.keySetting = keySetting;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            if (!isListening) {
                if (button == 0) {
                    isListening = true;
                    return true;
                }
            } else {
                // Accept mouse button as keybind
                if (button >= 0 && button <= 7) { // GLFW supports up to 8 mouse buttons
                    keySetting.setValue(button + 1000); // Offset to avoid collision with key codes
                    isListening = false;
                    return true;
                }
                if (button != 0) { // Right click or middle click to cancel
                    isListening = false;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isListening) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                isListening = false;
                return true;
            } else if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                keySetting.setValue(0); // Reset to none
                isListening = false;
                return true;
            } else {
                keySetting.setValue(keyCode);
                isListening = false;
                return true;
            }
        }
        return false;
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        FontAtlas font = Chorus.getInstance().getFonts().getInterSemiBold();
        
        // Update hover state
        isHovered = isHovered(mouseX, mouseY);
        
        // Draw background
        Color backgroundColor = isHovered ? new Color(50, 50, 50, 200) : new Color(30, 30, 30, 200);
        Render2DEngine.drawRoundedRect(context.getMatrices(), x, y, width, height, 3, backgroundColor);
        
        // Draw border
        Color borderColor = isListening ? new Color(100, 150, 255, 255) : new Color(100, 100, 100, 150);
        Render2DEngine.drawRoundedOutline(context.getMatrices(), x, y, width, height, 3, 1, borderColor);
        
        // Draw text
        String text;
        if (isListening) {
            text = "Listening...";
        } else {
            String keyName = keySetting.getKeyName();
            text = keyName.equals("None") ? "None" : keyName;
        }
        
        float textX = x + (width - font.getWidth(text, 6)) / 2;
        float textY = y + (height - font.getLineHeight(6)) / 2;
        
        Color textColor = isListening ? new Color(100, 150, 255, 255) : new Color(255, 255, 255, 255);
        font.render(context.getMatrices(), text, textX, textY, 6, textColor.getRGB());
    }

    private boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }



    public boolean isListening() {
        return isListening;
    }

    public void setListening(boolean listening) {
        this.isListening = listening;
    }
    
    public void updatePosition(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

} 
