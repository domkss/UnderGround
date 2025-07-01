package dev.domkss.screen;

import dev.domkss.UnderGround;
import dev.domkss.networking.PacketHandler;
import dev.domkss.networking.payloads.RequestSkillsDataIncreasePayload;
import dev.domkss.networking.payloads.SkillsDataPayload;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SkillScreen extends Screen {
    private final List<StatEntry> stats = new ArrayList<>();
    private final int PANEL_WIDTH = 300;
    private final int PANEL_HEIGHT = 240;

    private int scrollOffset = 0;
    private int maxScroll = 0;

    public SkillScreen(SkillsDataPayload.SkillsData skillsData) {
        super(Text.of("Skills"));

        stats.add(new StatEntry("Health", skillsData.health().getFirst(), skillsData.health().getSecond(), Identifier.of("minecraft", "textures/mob_effect/health_boost.png")));
        stats.add(new StatEntry("Armor", skillsData.armor().getFirst(), skillsData.armor().getSecond(), Identifier.of("minecraft", "textures/item/diamond_chestplate.png")));
        stats.add(new StatEntry("Speed", skillsData.speed().getFirst(), skillsData.speed().getSecond(), Identifier.of("minecraft", "textures/mob_effect/speed.png")));
        stats.add(new StatEntry("Haste", skillsData.haste().getFirst(), skillsData.haste().getSecond(), Identifier.of("minecraft", "textures/mob_effect/haste.png")));
        stats.add(new StatEntry("Resistance", skillsData.radiation_resistance().getFirst(), skillsData.radiation_resistance().getSecond(), Identifier.of(UnderGround.MOD_ID, "textures/mob_effect/radiation_effect.png")));
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
        Identifier backgroundTexture = Identifier.of(UnderGround.MOD_ID, "textures/gui/skill_screen_background.png");
        context.drawTexture(RenderLayer::getGuiTextured, backgroundTexture, panelX, panelY, 0, 0, PANEL_WIDTH+10, PANEL_HEIGHT, 310, 240);


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

        drawScrollbar(context, panelX, contentStartY, entryHeight * visibleRows, scrollOffset, maxScroll);

    }

    private void drawScrollbar(DrawContext context, int listX, int listY, int scrollbarHeight, int scrollOffset, int maxScroll) {
        int scrollbarX = listX + PANEL_WIDTH;
        int scrollbarWidth = 4;

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
        context.fill(x, y, x + 140, y + 29, 0x3fd6d6d4);

        // Icon
        context.drawTexture(RenderLayer::getGuiTextured, entry.icon, x + 3, y + 5, 0, 0, 16, 16, 16, 16);

        // Name and value
        context.drawText(textRenderer, Text.of(entry.name), x + 23, y + 10, 0x000000, false);
        context.drawText(textRenderer, Text.of(String.valueOf(entry.value) + "/" + String.valueOf(entry.maxValue)), x + 86, y + 10, 0x000000, false);

        //Plus button
        context.drawTexture(RenderLayer::getGuiTextured, PLUS_BUTTON, x + 120, y + 10, 0, 0, 9, 9, 9, 9);

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
            if (mouseX >= x + 120 && mouseX <= x + 129 && mouseY >= y + 10 && mouseY <= y + 19) {
                if (stats.get(statIndex).value < stats.get(statIndex).maxValue) {
                    this.requestStatIncrease(stats.get(statIndex).name);
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void requestStatIncrease(String statKey) {
        PacketHandler.sendToServer(new RequestSkillsDataIncreasePayload(statKey));
    }

    public void onStatIncreased(String statKey) {
        Optional<StatEntry> statEntry = stats.stream().filter(item -> item.name.equalsIgnoreCase(statKey)).findFirst();
        statEntry.ifPresent(entry -> entry.value++);
    }
}
