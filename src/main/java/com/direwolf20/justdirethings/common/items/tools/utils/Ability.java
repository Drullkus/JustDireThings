package com.direwolf20.justdirethings.common.items.tools.utils;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public enum Ability {
    //Tier 1
    MOBSCANNER(SettingType.TOGGLE, 10, 100), //TODO Configs
    OREMINER(SettingType.TOGGLE, 1, 10),
    ORESCANNER(SettingType.TOGGLE, 10, 100),
    LAWNMOWER(SettingType.TOGGLE, 1, 10),
    SKYSWEEPER(SettingType.TOGGLE, 1, 10),
    TREEFELLER(SettingType.TOGGLE, 1, 10),
    LEAFBREAKER(SettingType.TOGGLE, 1, 10),
    //Tier 2
    SMELTER(SettingType.TOGGLE, 1, 10),
    HAMMER(SettingType.CYCLE, 1, 10),
    //Tier 3
    OREXRAY(SettingType.TOGGLE, 100, 1000),
    DROPTELEPORT(SettingType.TOGGLE, 10, 100);

    public enum SettingType {
        TOGGLE,
        SLIDER,
        CYCLE
    }

    final String name;
    final String localization;
    final SettingType settingType;
    final ResourceLocation iconLocation;
    final int durabilityCost;
    final int feCost;
    // Dynamic parameter map
    private static final Map<Ability, AbilityParams> dynamicParams = new EnumMap<>(Ability.class);

    Ability(SettingType settingType, int durabilityCost, int feCost) {
        this.name = this.name().toLowerCase(Locale.ROOT);
        this.settingType = settingType;
        this.localization = "justdirethings.ability." + name;
        this.iconLocation = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/" + name + ".png");
        this.durabilityCost = durabilityCost;
        this.feCost = feCost;
    }

    public boolean hasDynamicParams(Ability toolAbility) {
        return dynamicParams.containsKey(toolAbility);
    }

    public String getLocalization() {
        return localization;
    }

    public String getName() {
        return name;
    }

    public SettingType getSettingType() {
        return settingType;
    }

    public ResourceLocation getIconLocation() {
        return iconLocation;
    }

    public int getDurabilityCost() {
        return durabilityCost;
    }

    public int getFeCost() {
        return feCost;
    }
}
