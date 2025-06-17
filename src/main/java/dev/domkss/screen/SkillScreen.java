package dev.domkss.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class SkillScreen extends Screen {
    private final List<StatEntry> stats = new ArrayList<>();
    private final int PANEL_WIDTH = 300;
    private final int PANEL_HEIGHT = 240;

    private int scrollOffset = 0;
    private int maxScroll = 0;

    public SkillScreen() {
        super(Text.of("Skills"));
        // Example stats
        stats.add(new StatEntry("Health",20, Identifier.of("minecraft", "textures/item/apple.png")));
        stats.add(new StatEntry("Speed",20, Identifier.of("minecraft", "textures/item/sugar.png")));
        stats.add(new StatEntry("Mining",20, Identifier.of("minecraft", "textures/item/iron_pickaxe.png")));
        stats.add(new StatEntry("Strength",20, Identifier.of("minecraft", "textures/item/iron_sword.png")));
        stats.add(new StatEntry("Luck",20, Identifier.of("minecraft", "textures/item/rabbit_foot.png")));
        stats.add(new StatEntry("Defense",20, Identifier.of("minecraft", "textures/item/shield.png")));
        // Add as many as needed
        stats.add(new StatEntry("Health",20, Identifier.of("minecraft", "textures/item/apple.png")));
        stats.add(new StatEntry("Speed",20, Identifier.of("minecraft", "textures/item/sugar.png")));
        stats.add(new StatEntry("Mining",20, Identifier.of("minecraft", "textures/item/iron_pickaxe.png")));
        stats.add(new StatEntry("Strength",20, Identifier.of("minecraft", "textures/item/iron_sword.png")));
        stats.add(new StatEntry("Luck",20, Identifier.of("minecraft", "textures/item/rabbit_foot.png")));
        stats.add(new StatEntry("Defense",20, Identifier.of("minecraft", "textures/item/shield.png")));

        stats.add(new StatEntry("Health",20, Identifier.of("minecraft", "textures/item/apple.png")));
        stats.add(new StatEntry("Speed",20, Identifier.of("minecraft", "textures/item/sugar.png")));
        stats.add(new StatEntry("Mining",20, Identifier.of("minecraft", "textures/item/iron_pickaxe.png")));
        stats.add(new StatEntry("Strength",20, Identifier.of("minecraft", "textures/item/iron_sword.png")));
        stats.add(new StatEntry("Luck",20, Identifier.of("minecraft", "textures/item/rabbit_foot.png")));
        stats.add(new StatEntry("Defense",20, Identifier.of("minecraft", "textures/item/shield.png")));
    }

    @Override
    protected void init() {
        super.init();
        scrollOffset = 0;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset -= (int) verticalAmount * 30;
        scrollOffset = MathHelper.clamp(scrollOffset, 0, maxScroll);
        return true;
    }



    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int centerX = width / 2;
        int centerY = height / 2;
        int panelX = centerX - PANEL_WIDTH / 2;
        int panelY = centerY - PANEL_HEIGHT / 2;

        // Background
        Identifier texture = Identifier.of("minecraft", "textures/gui/container/generic_54.png");
        //context.drawTexture(RenderLayer::getGuiTextured, texture, panelX, panelY, 7, 17, PANEL_WIDTH, PANEL_HEIGHT, 256, 256);

        // Summary section
        context.drawText(textRenderer, Text.of("Total Health: +" + getStatValue("Health")), panelX + 10, panelY + 10, 0x000000, false);
        context.drawText(textRenderer, Text.of("Total Speed: +" + getStatValue("Speed")), panelX + 10, panelY + 25, 0x000000, false);

        // Scrollable content area
        int contentStartY = panelY + 50;
        int entryHeight = 30;
        int entriesPerRow = 2;
        int totalRows = (int) Math.ceil(stats.size() / 2.0);
        int visibleRows = 3;
        maxScroll = Math.max(0, (totalRows - visibleRows) * entryHeight);

        int startIndex = (scrollOffset / entryHeight) * entriesPerRow;

        for (int i = 0; i < visibleRows * entriesPerRow; i++) {
            int statIndex = startIndex + i;
            if (statIndex >= stats.size()) break;

            int col = i % 2;
            int row = i / 2;
            int x = panelX + 10 + (col * (PANEL_WIDTH / 2));
            int y = contentStartY + (row * entryHeight) - (scrollOffset % entryHeight);

            drawStatEntry(context, stats.get(statIndex), x, y);
        }

        drawScrollbar(context,panelX,contentStartY,entryHeight*visibleRows,scrollOffset,maxScroll);

    }

    private void drawScrollbar(DrawContext context, int listX, int listY, int listHeight, int scrollOffset, int maxScroll) {
        int scrollbarX = listX + PANEL_WIDTH;
        int scrollbarWidth = 4;
        int scrollbarHeight = listHeight;

        // Track
        context.fill(scrollbarX, listY, scrollbarX + scrollbarWidth, listY + scrollbarHeight, 0xFFAAAAAA);

        // Thumb (based on scroll position)
        int thumbHeight = Math.max(20, scrollbarHeight * scrollbarHeight / (scrollbarHeight + maxScroll)); // size logic
        int thumbY = listY + (scrollOffset * (scrollbarHeight - thumbHeight)) / Math.max(1, maxScroll);
        context.fill(scrollbarX, thumbY, scrollbarX + scrollbarWidth, thumbY + thumbHeight, 0xFF666666);
    }

    private static final Identifier PLUS_BUTTON = Identifier.of("underground", "textures/gui/plus_button.png");

    private void drawStatEntry(DrawContext context, StatEntry entry, int x, int y) {

        // Background
        context.fill(x, y, x + 130, y + 29, 0xf5d5270d);

        // Icon
        context.drawTexture(RenderLayer::getGuiTextured,entry.icon, x+3, y+5, 0, 0, 16, 16, 16, 16);

        // Name and value
        context.drawText(textRenderer, Text.of(entry.name), x + 23, y+10, 0x000000, false);
        context.drawText(textRenderer, Text.of(String.valueOf(entry.value)+"/"+String.valueOf(entry.maxValue)), x + 70, y+10, 0x000000, false);

        //Plus button
        context.drawTexture(RenderLayer::getGuiTextured, PLUS_BUTTON, x + 110, y+10, 0, 0, 9, 9, 9, 9);

    }

    private int getStatValue(String name) {
        return stats.stream().filter(s -> s.name.equals(name)).mapToInt(s -> s.value).sum();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int centerX = width / 2;
        int panelX = centerX - PANEL_WIDTH / 2;
        int contentStartY = height / 2 - PANEL_HEIGHT / 2 + 50;
        int entryHeight = 30;
        int entriesPerRow = 2;
        int startIndex = (scrollOffset / entryHeight) * entriesPerRow;

        for (int i = 0; i < 12; i++) {
            int statIndex = startIndex + i;
            if (statIndex >= stats.size()) break;

            int col = i % 2;
            int row = i / 2;
            int x = panelX + 10 + (col * (PANEL_WIDTH / 2));
            int y = contentStartY + (row * entryHeight) - (scrollOffset % entryHeight);

            // If click is inside + button area
            if (mouseX >= x + 110 && mouseX <= x + 119 && mouseY >= y+10 && mouseY <= y + 19) {
                stats.get(statIndex).value++;
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
