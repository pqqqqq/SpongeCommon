/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.registry;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFishFood;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Game;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.attribute.Attribute;
import org.spongepowered.api.attribute.AttributeBuilder;
import org.spongepowered.api.attribute.AttributeCalculator;
import org.spongepowered.api.attribute.AttributeModifierBuilder;
import org.spongepowered.api.attribute.Operation;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.tile.Banner;
import org.spongepowered.api.block.tile.CommandBlock;
import org.spongepowered.api.block.tile.DaylightDetector;
import org.spongepowered.api.block.tile.EnchantmentTable;
import org.spongepowered.api.block.tile.EndPortal;
import org.spongepowered.api.block.tile.EnderChest;
import org.spongepowered.api.block.tile.MobSpawner;
import org.spongepowered.api.block.tile.Note;
import org.spongepowered.api.block.tile.Sign;
import org.spongepowered.api.block.tile.Skull;
import org.spongepowered.api.block.tile.TileEntityType;
import org.spongepowered.api.block.tile.carrier.BrewingStand;
import org.spongepowered.api.block.tile.carrier.Chest;
import org.spongepowered.api.block.tile.carrier.Dispenser;
import org.spongepowered.api.block.tile.carrier.Dropper;
import org.spongepowered.api.block.tile.carrier.Furnace;
import org.spongepowered.api.block.tile.carrier.Hopper;
import org.spongepowered.api.data.DataManipulatorRegistry;
import org.spongepowered.api.data.ImmutableDataRegistry;
import org.spongepowered.api.data.manipulators.DisplayNameData;
import org.spongepowered.api.data.manipulators.PotionEffectData;
import org.spongepowered.api.data.manipulators.RepresentedItemData;
import org.spongepowered.api.data.manipulators.blocks.DirectionalData;
import org.spongepowered.api.data.manipulators.blocks.PoweredData;
import org.spongepowered.api.data.manipulators.entities.CareerData;
import org.spongepowered.api.data.manipulators.entities.InvulnerabilityData;
import org.spongepowered.api.data.manipulators.entities.TradeOfferData;
import org.spongepowered.api.data.manipulators.items.EnchantmentData;
import org.spongepowered.api.data.manipulators.items.LoreData;
import org.spongepowered.api.data.manipulators.tileentities.BannerData;
import org.spongepowered.api.data.manipulators.tileentities.BeaconData;
import org.spongepowered.api.data.manipulators.tileentities.SignData;
import org.spongepowered.api.data.types.Art;
import org.spongepowered.api.data.types.Arts;
import org.spongepowered.api.data.types.BannerPatternShape;
import org.spongepowered.api.data.types.BannerPatternShapes;
import org.spongepowered.api.data.types.Career;
import org.spongepowered.api.data.types.Careers;
import org.spongepowered.api.data.types.CoalType;
import org.spongepowered.api.data.types.CoalTypes;
import org.spongepowered.api.data.types.Comparison;
import org.spongepowered.api.data.types.CookedFish;
import org.spongepowered.api.data.types.CookedFishes;
import org.spongepowered.api.data.types.DirtType;
import org.spongepowered.api.data.types.DisguisedBlockType;
import org.spongepowered.api.data.types.DyeColor;
import org.spongepowered.api.data.types.DyeColors;
import org.spongepowered.api.data.types.Fish;
import org.spongepowered.api.data.types.Fishes;
import org.spongepowered.api.data.types.GoldenApple;
import org.spongepowered.api.data.types.Hinge;
import org.spongepowered.api.data.types.HorseColor;
import org.spongepowered.api.data.types.HorseColors;
import org.spongepowered.api.data.types.HorseStyle;
import org.spongepowered.api.data.types.HorseStyles;
import org.spongepowered.api.data.types.HorseVariant;
import org.spongepowered.api.data.types.HorseVariants;
import org.spongepowered.api.data.types.NotePitch;
import org.spongepowered.api.data.types.NotePitches;
import org.spongepowered.api.data.types.OcelotType;
import org.spongepowered.api.data.types.OcelotTypes;
import org.spongepowered.api.data.types.PlantType;
import org.spongepowered.api.data.types.PortionType;
import org.spongepowered.api.data.types.PrismarineType;
import org.spongepowered.api.data.types.Profession;
import org.spongepowered.api.data.types.Professions;
import org.spongepowered.api.data.types.QuartzType;
import org.spongepowered.api.data.types.RabbitType;
import org.spongepowered.api.data.types.RabbitTypes;
import org.spongepowered.api.data.types.RailDirection;
import org.spongepowered.api.data.types.SandstoneType;
import org.spongepowered.api.data.types.SkeletonType;
import org.spongepowered.api.data.types.SkeletonTypes;
import org.spongepowered.api.data.types.SkullType;
import org.spongepowered.api.data.types.SkullTypes;
import org.spongepowered.api.data.types.SlabType;
import org.spongepowered.api.data.types.StairShape;
import org.spongepowered.api.data.types.StoneType;
import org.spongepowered.api.data.types.TreeType;
import org.spongepowered.api.data.types.WallType;
import org.spongepowered.api.effect.particle.ParticleEffectBuilder;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.EntityInteractionType;
import org.spongepowered.api.entity.EntityInteractionTypes;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.player.gamemode.GameMode;
import org.spongepowered.api.entity.player.gamemode.GameModes;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.FireworkEffectBuilder;
import org.spongepowered.api.item.FireworkShape;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStackBuilder;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.item.merchant.TradeOffer;
import org.spongepowered.api.item.merchant.TradeOfferBuilder;
import org.spongepowered.api.item.recipe.RecipeRegistry;
import org.spongepowered.api.potion.PotionEffectBuilder;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.potion.PotionEffectTypes;
import org.spongepowered.api.resourcepack.ResourcePack;
import org.spongepowered.api.scoreboard.ScoreboardBuilder;
import org.spongepowered.api.scoreboard.TeamBuilder;
import org.spongepowered.api.scoreboard.Visibility;
import org.spongepowered.api.scoreboard.critieria.Criterion;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.scoreboard.objective.ObjectiveBuilder;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;
import org.spongepowered.api.service.persistence.SerializationService;
import org.spongepowered.api.stats.BlockStatistic;
import org.spongepowered.api.stats.EntityStatistic;
import org.spongepowered.api.stats.ItemStatistic;
import org.spongepowered.api.stats.Statistic;
import org.spongepowered.api.stats.StatisticBuilder;
import org.spongepowered.api.stats.StatisticFormat;
import org.spongepowered.api.stats.StatisticGroup;
import org.spongepowered.api.stats.TeamStatistic;
import org.spongepowered.api.stats.achievement.Achievement;
import org.spongepowered.api.stats.achievement.AchievementBuilder;
import org.spongepowered.api.status.Favicon;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.selector.SelectorType;
import org.spongepowered.api.text.translation.Translation;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.rotation.Rotation;
import org.spongepowered.api.util.rotation.Rotations;
import org.spongepowered.api.world.Dimension;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.DimensionTypes;
import org.spongepowered.api.world.GeneratorType;
import org.spongepowered.api.world.GeneratorTypes;
import org.spongepowered.api.world.WorldBuilder;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.api.world.difficulty.Difficulties;
import org.spongepowered.api.world.difficulty.Difficulty;
import org.spongepowered.api.world.gamerule.DefaultGameRules;
import org.spongepowered.api.world.gen.PopulatorFactory;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.storage.WorldProperties;
import org.spongepowered.api.world.weather.Weather;
import org.spongepowered.api.world.weather.Weathers;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.SpongeGame;
import org.spongepowered.common.configuration.SpongeConfig;
import org.spongepowered.common.data.SpongeImmutableRegistry;
import org.spongepowered.common.data.SpongeManipulatorRegistry;
import org.spongepowered.common.data.builders.block.data.SpongePatternLayerBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeBannerBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeBrewingStandBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeChestBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeCommandBlockBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeComparatorBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeDaylightBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeDispenserBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeDropperBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeEnchantmentTableBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeEndPortalBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeEnderChestBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeFurnaceBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeHopperBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeMobSpawnerBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeNoteBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeSignBuilder;
import org.spongepowered.common.data.builders.block.tile.SpongeSkullBuilder;
import org.spongepowered.common.data.manipulators.SpongeDisplayNameData;
import org.spongepowered.common.data.manipulators.SpongePotionEffectData;
import org.spongepowered.common.data.manipulators.SpongeRepresentedItemData;
import org.spongepowered.common.data.manipulators.SpongeTradeOfferData;
import org.spongepowered.common.data.manipulators.blocks.SpongeDirectionalData;
import org.spongepowered.common.data.manipulators.blocks.SpongePoweredData;
import org.spongepowered.common.data.manipulators.entities.SpongeCareerData;
import org.spongepowered.common.data.manipulators.entities.SpongeInvulnerabilityData;
import org.spongepowered.common.data.manipulators.items.SpongeEnchantmentItemData;
import org.spongepowered.common.data.manipulators.items.SpongeLoreData;
import org.spongepowered.common.data.manipulators.tiles.SpongeBannerData;
import org.spongepowered.common.data.manipulators.tiles.SpongeBeaconData;
import org.spongepowered.common.data.manipulators.tiles.SpongeSignData;
import org.spongepowered.common.data.types.SpongeCookedFish;
import org.spongepowered.common.data.types.SpongeNotePitch;
import org.spongepowered.common.data.types.SpongeSkullType;
import org.spongepowered.common.data.utils.SpongeBannerDataBuilder;
import org.spongepowered.common.data.utils.SpongeBeaconDataBuilder;
import org.spongepowered.common.data.utils.SpongeDisplayNameDataBuilder;
import org.spongepowered.common.data.utils.SpongePotionDataBuilder;
import org.spongepowered.common.data.utils.SpongeRepresentedItemBuilder;
import org.spongepowered.common.data.utils.SpongeSignDataBuilder;
import org.spongepowered.common.data.utils.blocks.SpongeDirectionalUtil;
import org.spongepowered.common.data.utils.blocks.SpongePoweredUtil;
import org.spongepowered.common.data.utils.entities.SpongeCareerDataUtil;
import org.spongepowered.common.data.utils.entities.SpongeInvulnerabilityUtil;
import org.spongepowered.common.data.utils.entities.SpongeTradeOfferUtil;
import org.spongepowered.common.data.utils.items.SpongeEnchantmentUtil;
import org.spongepowered.common.data.utils.items.SpongeLoreUtil;
import org.spongepowered.common.effect.particle.SpongeParticleEffectBuilder;
import org.spongepowered.common.effect.particle.SpongeParticleType;
import org.spongepowered.common.effect.sound.SpongeSound;
import org.spongepowered.common.entity.SpongeCareer;
import org.spongepowered.common.entity.SpongeEntityConstants;
import org.spongepowered.common.entity.SpongeEntityInteractionType;
import org.spongepowered.common.entity.SpongeEntityMeta;
import org.spongepowered.common.entity.SpongeEntityType;
import org.spongepowered.common.entity.SpongeProfession;
import org.spongepowered.common.entity.player.gamemode.SpongeGameMode;
import org.spongepowered.common.item.SpongeCoalType;
import org.spongepowered.common.item.SpongeFireworkBuilder;
import org.spongepowered.common.item.SpongeItemStackBuilder;
import org.spongepowered.common.item.merchant.SpongeTradeOfferBuilder;
import org.spongepowered.common.potion.SpongePotionBuilder;
import org.spongepowered.common.rotation.SpongeRotation;
import org.spongepowered.common.status.SpongeFavicon;
import org.spongepowered.common.text.SpongeTextFactory;
import org.spongepowered.common.text.chat.SpongeChatType;
import org.spongepowered.common.text.format.SpongeTextColor;
import org.spongepowered.common.text.format.SpongeTextStyle;
import org.spongepowered.common.text.translation.SpongeTranslation;
import org.spongepowered.common.weather.SpongeWeather;
import org.spongepowered.common.world.SpongeDimensionType;
import org.spongepowered.common.world.SpongeWorldBuilder;
import org.spongepowered.common.world.gen.WorldGeneratorRegistry;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class SpongeGameRegistry implements GameRegistry {

    private final Map<String, BiomeType> biomeTypeMappings = Maps.newHashMap();

    public static final Map<Class<? extends WorldProvider>, SpongeConfig<SpongeConfig.DimensionConfig>> dimensionConfigs = Maps.newHashMap();

    public static final Map<String, TextColor> textColorMappings = Maps.newHashMap();
    public static final Map<EnumChatFormatting, SpongeTextColor> enumChatColor = Maps.newEnumMap(EnumChatFormatting.class);

    public static final ImmutableMap<String, TextStyle> textStyleMappings = new ImmutableMap.Builder<String, TextStyle>()
            .put("BOLD", SpongeTextStyle.of(EnumChatFormatting.BOLD))
            .put("ITALIC", SpongeTextStyle.of(EnumChatFormatting.ITALIC))
            .put("UNDERLINE", SpongeTextStyle.of(EnumChatFormatting.UNDERLINE))
            .put("STRIKETHROUGH", SpongeTextStyle.of(EnumChatFormatting.STRIKETHROUGH))
            .put("OBFUSCATED", SpongeTextStyle.of(EnumChatFormatting.OBFUSCATED))
            .put("RESET", SpongeTextStyle.of(EnumChatFormatting.RESET))
            .build();
    private static final ImmutableMap<String, ChatType> chatTypeMappings = new ImmutableMap.Builder<String, ChatType>()
            .put("CHAT", new SpongeChatType((byte) 0))
            .put("SYSTEM", new SpongeChatType((byte) 1))
            .put("ACTION_BAR", new SpongeChatType((byte) 2))
            .build();

    private static final ImmutableMap<String, Rotation> rotationMappings = new ImmutableMap.Builder<String, Rotation>()
            .put("TOP", new SpongeRotation(0))
            .put("TOP_RIGHT", new SpongeRotation(45))
            .put("RIGHT", new SpongeRotation(90))
            .put("BOTTOM_RIGHT", new SpongeRotation(135))
            .put("BOTTOM", new SpongeRotation(180))
            .put("BOTTOM_LEFT", new SpongeRotation(225))
            .put("LEFT", new SpongeRotation(270))
            .put("TOP_LEFT", new SpongeRotation(315))
            .build();
    public static final ImmutableBiMap<Direction, EnumFacing> directionMap = ImmutableBiMap.<Direction, EnumFacing>builder()
            .put(Direction.NORTH, EnumFacing.NORTH)
            .put(Direction.EAST, EnumFacing.EAST)
            .put(Direction.SOUTH, EnumFacing.SOUTH)
            .put(Direction.WEST, EnumFacing.WEST)
            .put(Direction.UP, EnumFacing.UP)
            .put(Direction.DOWN, EnumFacing.DOWN)
            .build();
    public static final ImmutableMap<String, GameMode> gameModeMappings = new ImmutableMap.Builder<String, GameMode>()
            .put("SURVIVAL", new SpongeGameMode("SURVIVAL"))
            .put("CREATIVE", new SpongeGameMode("CREATIVE"))
            .put("ADVENTURE", new SpongeGameMode("ADVENTURE"))
            .put("SPECTATOR", new SpongeGameMode("SPECTATOR"))
            .put("NOT_SET", new SpongeGameMode("NOT_SET"))
            .build();
    private static final ImmutableMap<String, Difficulty> difficultyMappings = new ImmutableMap.Builder<String, Difficulty>()
            .put("PEACEFUL", (Difficulty) (Object) EnumDifficulty.PEACEFUL)
            .put("EASY", (Difficulty) (Object) EnumDifficulty.EASY)
            .put("NORMAL", (Difficulty) (Object) EnumDifficulty.NORMAL)
            .put("HARD", (Difficulty) (Object) EnumDifficulty.HARD)
            .build();
    public final Map<String, SpongeEntityType> entityIdToTypeMappings = Maps.newHashMap();
    public final Map<Class<? extends Entity>, SpongeEntityType> entityClassToTypeMappings = Maps.newHashMap();
    public final Map<String, Enchantment> enchantmentMappings = Maps.newHashMap();
    private final Map<String, Career> careerMappings = Maps.newHashMap();
    private final Map<String, Profession> professionMappings = Maps.newHashMap();
    private final Map<Integer, List<Career>> professionToCareerMappings = Maps.newHashMap();
    private final Map<String, DimensionType> dimensionTypeMappings = Maps.newHashMap();
    public final Map<Class<? extends Dimension>, DimensionType> dimensionClassMappings = Maps.newHashMap();
    private final Map<String, SpongeParticleType> particleMappings = Maps.newHashMap();
    private final Map<String, ParticleType> particleByName = Maps.newHashMap();
    private final List<PotionEffectType> potionList = new ArrayList<PotionEffectType>();
    private final List<BiomeType> biomeTypes = new ArrayList<BiomeType>();
    private final Map<String, SoundType> soundNames = Maps.newHashMap();
    private final Map<String, CoalType> coaltypeMappings = Maps.newHashMap();
    private final Map<String, GoldenApple> goldenAppleMappings = Maps.newHashMap();
    private final WorldGeneratorRegistry worldGeneratorRegistry = new WorldGeneratorRegistry();
    private final Hashtable<Class<? extends WorldProvider>, Integer> classToProviders = new Hashtable<Class<? extends WorldProvider>, Integer>();
    private final Map<UUID, WorldProperties> worldPropertiesUuidMappings = Maps.newHashMap();
    private final Map<String, WorldProperties> worldPropertiesNameMappings = Maps.newHashMap();
    private final Map<Integer, String> worldFolderDimensionIdMappings = Maps.newHashMap();
    public final Map<UUID, String> worldFolderUniqueIdMappings = Maps.newHashMap();

    private final Map<String, NotePitch> notePitchMappings = Maps.newHashMap();
    private final Map<String, SkullType> skullTypeMappings = Maps.newHashMap();
    private final Map<String, BannerPatternShape> bannerPatternShapeMappings = Maps.newHashMap();
    private final Map<String, BannerPatternShape> idToBannerPatternShapeMappings = Maps.newHashMap();
    private final Map<String, Fish> fishMappings = Maps.newHashMap();
    private final Map<String, CookedFish> cookedFishMappings = Maps.newHashMap();
    private final Map<String, DyeColor> dyeColorMappings = Maps.newHashMap();
    private final Map<String, Art> artMappings = Maps.newHashMap();
    private final Map<String, EntityType> entityTypeMappings = Maps.newHashMap();

    private static final ImmutableMap<String, EntityInteractionType> entityInteractionTypeMappings =
            new ImmutableMap.Builder<String, EntityInteractionType>()
                    .put("ATTACK", new SpongeEntityInteractionType("ATTACK"))
                    .put("PICK_BLOCK", new SpongeEntityInteractionType("PICK_BLOCK"))
                    .put("USE", new SpongeEntityInteractionType("USE"))
                    .build();
    protected Map<Class<? extends CatalogType>, Map<String, ? extends CatalogType>> catalogTypeMap =
            ImmutableMap.<Class<? extends CatalogType>, Map<String, ? extends CatalogType>>builder()
                    .put(Achievement.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Art.class, this.artMappings)
                    .put(Attribute.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(BiomeType.class, this.biomeTypeMappings)
                    .put(BlockType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Career.class, this.careerMappings)
                    .put(ChatType.class, chatTypeMappings)
                    .put(BannerPatternShape.class, this.bannerPatternShapeMappings)
                    .put(CookedFish.class, this.cookedFishMappings)
                    .put(DyeColor.class, this.dyeColorMappings)
                    .put(EntityInteractionType.class, entityInteractionTypeMappings)
                    .put(CoalType.class, this.coaltypeMappings)
                    .put(NotePitch.class, this.notePitchMappings)
                    .put(Comparison.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Criterion.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Difficulty.class, difficultyMappings)
                    .put(DimensionType.class, this.dimensionTypeMappings)
                    .put(DirtType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(DisguisedBlockType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Enchantment.class, this.enchantmentMappings)
                    .put(EquipmentType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(FireworkShape.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Fish.class, this.fishMappings)
                    .put(GameMode.class, gameModeMappings)
                    .put(GoldenApple.class, this.goldenAppleMappings)
                    .put(EntityType.class, this.entityTypeMappings)
                    .put(Hinge.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(HorseColor.class, SpongeEntityConstants.HORSE_COLORS)
                    .put(HorseStyle.class, SpongeEntityConstants.HORSE_STYLES)
                    .put(HorseVariant.class, SpongeEntityConstants.HORSE_VARIANTS)
                    .put(ItemType.class, ImmutableMap.<String, CatalogType>of()) // TODO handle special case of items
                    .put(ObjectiveDisplayMode.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(OcelotType.class, SpongeEntityConstants.OCELOT_TYPES)
                    .put(Operation.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(ParticleType.class, this.particleByName)
                    .put(PlantType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(PotionEffectType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(PortionType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(PrismarineType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Profession.class, this.professionMappings)
                    .put(QuartzType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(RabbitType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(RailDirection.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Rotation.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(SandstoneType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(SelectorType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(SkeletonType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(SkullType.class, this.skullTypeMappings)
                    .put(SlabType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(SoundType.class, this.soundNames)
                    .put(StairShape.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Statistic.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(StatisticFormat.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(StatisticGroup.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(StoneType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(TextColor.class, textColorMappings)
                    .put(TileEntityType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(TreeType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Visibility.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(WallType.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(Weather.class, ImmutableMap.<String, CatalogType>of()) // TODO
                    .put(WorldGeneratorModifier.class, this.worldGeneratorRegistry.viewModifiersMap())
                    .build();
    private final Map<Class<?>, Class<?>> builderMap = ImmutableMap.of(); // TODO FIGURE OUT HOW TO DO THIS!!?!

    public com.google.common.base.Optional<PotionEffectType> getPotion(String id) {
        return com.google.common.base.Optional.fromNullable((PotionEffectType) Potion.getPotionFromResourceLocation(id));
    }

    public com.google.common.base.Optional<EntityType> getEntity(String id) {
        if (!id.contains(":")) {
            id = "minecraft:" + id;
        }
        return com.google.common.base.Optional.fromNullable((EntityType) this.entityIdToTypeMappings.get(id));
    }

    public com.google.common.base.Optional<BiomeType> getBiome(String id) {
        for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray()) {
            if (biome != null && biome.biomeName.equalsIgnoreCase(id)) {
                return com.google.common.base.Optional.of((BiomeType) biome);
            }
        }
        return com.google.common.base.Optional.absent();
    }

    public List<BiomeType> getBiomes() {
        return ImmutableList.copyOf(this.biomeTypes);
    }

    @Override
    public <T extends CatalogType> com.google.common.base.Optional<T> getType(Class<T> typeClass, String id) {
        Map<String, ? extends CatalogType> tempMap = this.catalogTypeMap.get(checkNotNull(typeClass, "null type class"));
        if (tempMap == null) {
            return com.google.common.base.Optional.absent();
        } else {
            T type = (T) tempMap.get(id);
            if (type == null) {
                return com.google.common.base.Optional.absent();
            } else {
                return com.google.common.base.Optional.of(type);
            }
        }
    }

    @Override
    public <T extends CatalogType> Collection<T> getAllOf(Class<T> typeClass) {
        Map<String, ? extends CatalogType> tempMap = this.catalogTypeMap.get(checkNotNull(typeClass, "null type class"));
        if (tempMap == null) {
            return Collections.emptyList();
        } else {
            ImmutableList.Builder<T> builder = ImmutableList.builder();
            for (Map.Entry<String, ? extends CatalogType> entry : tempMap.entrySet()) {
                builder.add((T) entry.getValue());
            }
            return builder.build();
        }
    }

    @Override
    public <T> com.google.common.base.Optional<T> getBuilderOf(Class<T> builderClass) {
        return null;
    }


    @Override
    public ItemStackBuilder getItemBuilder() {
        return new SpongeItemStackBuilder();
    }

    @Override
    public TradeOfferBuilder getTradeOfferBuilder() {
        return new SpongeTradeOfferBuilder();
    }

    @Override
    public FireworkEffectBuilder getFireworkEffectBuilder() {
        return new SpongeFireworkBuilder();
    }

    @Override
    public PotionEffectBuilder getPotionEffectBuilder() {
        return new SpongePotionBuilder();
    }

    @Override
    public ObjectiveBuilder getObjectiveBuilder() {
        return null;
    }

    @Override
    public TeamBuilder getTeamBuilder() {
        return null;
    }

    @Override
    public ScoreboardBuilder getScoreboardBuilder() {
        return null;
    }

    @Override
    public StatisticBuilder getStatisticBuilder() {
        return null;
    }

    @Override
    public StatisticBuilder.EntityStatisticBuilder getEntityStatisticBuilder() {
        return null;
    }

    @Override
    public StatisticBuilder.BlockStatisticBuilder getBlockStatisticBuilder() {
        return null;
    }

    @Override
    public StatisticBuilder.ItemStatisticBuilder getItemStatisticBuilder() {
        return null;
    }

    @Override
    public StatisticBuilder.TeamStatisticBuilder getTeamStatisticBuilder() {
        return null;
    }

    @Override
    public AchievementBuilder getAchievementBuilder() {
        return null;
    }

    @Override
    public AttributeModifierBuilder getAttributeModifierBuilder() {
        return null;
    }

    @Override
    public AttributeBuilder getAttributeBuilder() {
        return null; // TODO
    }

    @Override
    public WorldBuilder getWorldBuilder() {
        return new SpongeWorldBuilder();
    }

    @Override
    public ParticleEffectBuilder getParticleEffectBuilder(ParticleType particle) {
        checkNotNull(particle);

        if (particle instanceof SpongeParticleType.Colorable) {
            return new SpongeParticleEffectBuilder.BuilderColorable((SpongeParticleType.Colorable) particle);
        } else if (particle instanceof SpongeParticleType.Resizable) {
            return new SpongeParticleEffectBuilder.BuilderResizable((SpongeParticleType.Resizable) particle);
        } else if (particle instanceof SpongeParticleType.Note) {
            return new SpongeParticleEffectBuilder.BuilderNote((SpongeParticleType.Note) particle);
        } else if (particle instanceof SpongeParticleType.Material) {
            return new SpongeParticleEffectBuilder.BuilderMaterial((SpongeParticleType.Material) particle);
        } else {
            return new SpongeParticleEffectBuilder((SpongeParticleType) particle);
        }
    }

    @Override
    public List<String> getDefaultGameRules() {

        List<String> gameruleList = new ArrayList<String>();
        for (Field f : DefaultGameRules.class.getFields()) {
            try {
                gameruleList.add((String) f.get(null));
            } catch (Exception e) {
                //Ignoring error
            }
        }
        return gameruleList;
    }

    @Override
    public List<Career> getCareers(Profession profession) {
        return this.professionToCareerMappings.get(((SpongeEntityMeta) profession).type);
    }


    public List<DimensionType> getDimensionTypes() {
        return ImmutableList.copyOf(this.dimensionTypeMappings.values());
    }

    public void registerDimensionType(DimensionType type) {
        this.dimensionTypeMappings.put(type.getName(), type);
        this.dimensionClassMappings.put(type.getDimensionClass(), type);
    }

    public void registerWorldProperties(WorldProperties properties) {
        this.worldPropertiesUuidMappings.put(properties.getUniqueId(), properties);
        this.worldPropertiesNameMappings.put(properties.getWorldName(), properties);
    }

    public void registerWorldDimensionId(int dim, String folderName) {
        this.worldFolderDimensionIdMappings.put(dim, folderName);
    }

    public void registerWorldUniqueId(UUID uuid, String folderName) {
        this.worldFolderUniqueIdMappings.put(uuid, folderName);
    }

    public com.google.common.base.Optional<WorldProperties> getWorldProperties(String worldName) {
        return com.google.common.base.Optional.fromNullable(this.worldPropertiesNameMappings.get(worldName));
    }

    public Collection<WorldProperties> getAllWorldProperties() {
        return Collections.unmodifiableCollection(this.worldPropertiesNameMappings.values());
    }

    public String getWorldFolder(int dim) {
        return this.worldFolderDimensionIdMappings.get(dim);
    }

    public String getWorldFolder(UUID uuid) {
        return this.worldFolderUniqueIdMappings.get(uuid);
    }

    public int getProviderType(Class<? extends WorldProvider> provider) {
        return this.classToProviders.get(provider);
    }

    public WorldSettings.GameType getGameType(GameMode mode) {
        return WorldSettings.GameType.getByName(mode.getTranslation().getId());
    }

    public com.google.common.base.Optional<WorldProperties> getWorldProperties(UUID uuid) {
        return com.google.common.base.Optional.fromNullable(this.worldPropertiesUuidMappings.get(uuid));
    }

    @Override
    public void registerWorldGeneratorModifier(WorldGeneratorModifier modifier) {
        this.worldGeneratorRegistry.registerModifier(modifier);
    }

    public WorldGeneratorRegistry getWorldGeneratorRegistry() {
        return this.worldGeneratorRegistry;
    }

    @Override
    public com.google.common.base.Optional<Rotation> getRotationFromDegree(int degrees) {
        for (Rotation rotation : rotationMappings.values()) {
            if (rotation.getAngle() == degrees) {
                return com.google.common.base.Optional.of(rotation);
            }
        }
        return com.google.common.base.Optional.absent();
    }


    @Override
    public GameProfile createGameProfile(UUID uuid, String name) {
        return (GameProfile) new com.mojang.authlib.GameProfile(uuid, name);
    }

    @Override
    public Favicon loadFavicon(String raw) throws IOException {
        return SpongeFavicon.load(raw);
    }

    @Override
    public Favicon loadFavicon(File file) throws IOException {
        return SpongeFavicon.load(file);
    }

    @Override
    public Favicon loadFavicon(URL url) throws IOException {
        return SpongeFavicon.load(url);
    }

    @Override
    public Favicon loadFavicon(InputStream in) throws IOException {
        return SpongeFavicon.load(in);
    }

    @Override
    public Favicon loadFavicon(BufferedImage image) throws IOException {
        return SpongeFavicon.load(image);
    }


    @Override
    public RecipeRegistry getRecipeRegistry() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public DataManipulatorRegistry getManipulatorRegistry() {
        return SpongeManipulatorRegistry.getInstance();
    }

    @Override
    public ImmutableDataRegistry getImmutableDataRegistry() {
        return SpongeImmutableRegistry.getInstance();
    }

    @Override
    public AttributeCalculator getAttributeCalculator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public com.google.common.base.Optional<Translation> getTranslationById(String id) {
        return com.google.common.base.Optional.<Translation>of(new SpongeTranslation(id));
    }

    private void setParticles() {
        this.addParticleType("EXPLOSION_NORMAL", new SpongeParticleType(EnumParticleTypes.EXPLOSION_NORMAL, true));
        this.addParticleType("EXPLOSION_LARGE", new SpongeParticleType.Resizable(EnumParticleTypes.EXPLOSION_LARGE, 1f));
        this.addParticleType("EXPLOSION_HUGE", new SpongeParticleType(EnumParticleTypes.EXPLOSION_HUGE, false));
        this.addParticleType("FIREWORKS_SPARK", new SpongeParticleType(EnumParticleTypes.FIREWORKS_SPARK, true));
        this.addParticleType("WATER_BUBBLE", new SpongeParticleType(EnumParticleTypes.WATER_BUBBLE, true));
        this.addParticleType("WATER_SPLASH", new SpongeParticleType(EnumParticleTypes.WATER_SPLASH, true));
        this.addParticleType("WATER_WAKE", new SpongeParticleType(EnumParticleTypes.WATER_WAKE, true));
        this.addParticleType("SUSPENDED", new SpongeParticleType(EnumParticleTypes.SUSPENDED, false));
        this.addParticleType("SUSPENDED_DEPTH", new SpongeParticleType(EnumParticleTypes.SUSPENDED_DEPTH, false));
        this.addParticleType("CRIT", new SpongeParticleType(EnumParticleTypes.CRIT, true));
        this.addParticleType("CRIT_MAGIC", new SpongeParticleType(EnumParticleTypes.CRIT_MAGIC, true));
        this.addParticleType("SMOKE_NORMAL", new SpongeParticleType(EnumParticleTypes.SMOKE_NORMAL, true));
        this.addParticleType("SMOKE_LARGE", new SpongeParticleType(EnumParticleTypes.SMOKE_LARGE, true));
        this.addParticleType("SPELL", new SpongeParticleType(EnumParticleTypes.SPELL, false));
        this.addParticleType("SPELL_INSTANT", new SpongeParticleType(EnumParticleTypes.SPELL_INSTANT, false));
        this.addParticleType("SPELL_MOB", new SpongeParticleType.Colorable(EnumParticleTypes.SPELL_MOB, Color.BLACK));
        this.addParticleType("SPELL_MOB_AMBIENT", new SpongeParticleType.Colorable(EnumParticleTypes.SPELL_MOB_AMBIENT, Color.BLACK));
        this.addParticleType("SPELL_WITCH", new SpongeParticleType(EnumParticleTypes.SPELL_WITCH, false));
        this.addParticleType("DRIP_WATER", new SpongeParticleType(EnumParticleTypes.DRIP_WATER, false));
        this.addParticleType("DRIP_LAVA", new SpongeParticleType(EnumParticleTypes.DRIP_LAVA, false));
        this.addParticleType("VILLAGER_ANGRY", new SpongeParticleType(EnumParticleTypes.VILLAGER_ANGRY, false));
        this.addParticleType("VILLAGER_HAPPY", new SpongeParticleType(EnumParticleTypes.VILLAGER_HAPPY, true));
        this.addParticleType("TOWN_AURA", new SpongeParticleType(EnumParticleTypes.TOWN_AURA, true));
        this.addParticleType("NOTE", new SpongeParticleType.Note(EnumParticleTypes.NOTE, 0f));
        this.addParticleType("PORTAL", new SpongeParticleType(EnumParticleTypes.PORTAL, true));
        this.addParticleType("ENCHANTMENT_TABLE", new SpongeParticleType(EnumParticleTypes.ENCHANTMENT_TABLE, true));
        this.addParticleType("FLAME", new SpongeParticleType(EnumParticleTypes.FLAME, true));
        this.addParticleType("LAVA", new SpongeParticleType(EnumParticleTypes.LAVA, false));
        this.addParticleType("FOOTSTEP", new SpongeParticleType(EnumParticleTypes.FOOTSTEP, false));
        this.addParticleType("CLOUD", new SpongeParticleType(EnumParticleTypes.CLOUD, true));
        this.addParticleType("REDSTONE", new SpongeParticleType.Colorable(EnumParticleTypes.REDSTONE, Color.RED));
        this.addParticleType("SNOWBALL", new SpongeParticleType(EnumParticleTypes.SNOWBALL, false));
        this.addParticleType("SNOW_SHOVEL", new SpongeParticleType(EnumParticleTypes.SNOW_SHOVEL, true));
        this.addParticleType("SLIME", new SpongeParticleType(EnumParticleTypes.SLIME, false));
        this.addParticleType("HEART", new SpongeParticleType(EnumParticleTypes.HEART, false));
        this.addParticleType("BARRIER", new SpongeParticleType(EnumParticleTypes.BARRIER, false));
        this.addParticleType("ITEM_CRACK",
                new SpongeParticleType.Material(EnumParticleTypes.ITEM_CRACK, new net.minecraft.item.ItemStack(Blocks.air), true));
        this.addParticleType("BLOCK_CRACK",
                new SpongeParticleType.Material(EnumParticleTypes.BLOCK_CRACK, new net.minecraft.item.ItemStack(Blocks.air), true));
        this.addParticleType("BLOCK_DUST",
                new SpongeParticleType.Material(EnumParticleTypes.BLOCK_DUST, new net.minecraft.item.ItemStack(Blocks.air), true));
        this.addParticleType("WATER_DROP", new SpongeParticleType(EnumParticleTypes.WATER_DROP, false));
        // Is this particle available to be spawned? It's not registered on the client though
        this.addParticleType("ITEM_TAKE", new SpongeParticleType(EnumParticleTypes.ITEM_TAKE, false));
        this.addParticleType("MOB_APPEARANCE", new SpongeParticleType(EnumParticleTypes.MOB_APPEARANCE, false));

        RegistryHelper.mapFields(ParticleTypes.class, this.particleMappings);
    }

    private void addParticleType(String mapping, SpongeParticleType particle) {
        this.particleMappings.put(mapping, particle);
        this.particleByName.put(particle.getName(), particle);
    }

    private void setEnchantments() {
        this.enchantmentMappings.put("PROTECTION", (Enchantment) net.minecraft.enchantment.Enchantment.protection);
        this.enchantmentMappings.put("FIRE_PROTECTION", (Enchantment) net.minecraft.enchantment.Enchantment.fireProtection);
        this.enchantmentMappings.put("FEATHER_FALLING", (Enchantment) net.minecraft.enchantment.Enchantment.featherFalling);
        this.enchantmentMappings.put("BLAST_PROTECTION", (Enchantment) net.minecraft.enchantment.Enchantment.blastProtection);
        this.enchantmentMappings.put("PROJECTILE_PROTECTION", (Enchantment) net.minecraft.enchantment.Enchantment.projectileProtection);
        this.enchantmentMappings.put("RESPIRATION", (Enchantment) net.minecraft.enchantment.Enchantment.respiration);
        this.enchantmentMappings.put("AQUA_AFFINITY", (Enchantment) net.minecraft.enchantment.Enchantment.aquaAffinity);
        this.enchantmentMappings.put("THORNS", (Enchantment) net.minecraft.enchantment.Enchantment.thorns);
        this.enchantmentMappings.put("DEPTH_STRIDER", (Enchantment) net.minecraft.enchantment.Enchantment.depthStrider);
        this.enchantmentMappings.put("SHARPNESS", (Enchantment) net.minecraft.enchantment.Enchantment.sharpness);
        this.enchantmentMappings.put("SMITE", (Enchantment) net.minecraft.enchantment.Enchantment.smite);
        this.enchantmentMappings.put("BANE_OF_ARTHROPODS", (Enchantment) net.minecraft.enchantment.Enchantment.baneOfArthropods);
        this.enchantmentMappings.put("KNOCKBACK", (Enchantment) net.minecraft.enchantment.Enchantment.knockback);
        this.enchantmentMappings.put("FIRE_ASPECT", (Enchantment) net.minecraft.enchantment.Enchantment.fireAspect);
        this.enchantmentMappings.put("LOOTING", (Enchantment) net.minecraft.enchantment.Enchantment.looting);
        this.enchantmentMappings.put("EFFICIENCY", (Enchantment) net.minecraft.enchantment.Enchantment.efficiency);
        this.enchantmentMappings.put("SILK_TOUCH", (Enchantment) net.minecraft.enchantment.Enchantment.silkTouch);
        this.enchantmentMappings.put("UNBREAKING", (Enchantment) net.minecraft.enchantment.Enchantment.unbreaking);
        this.enchantmentMappings.put("FORTUNE", (Enchantment) net.minecraft.enchantment.Enchantment.fortune);
        this.enchantmentMappings.put("POWER", (Enchantment) net.minecraft.enchantment.Enchantment.power);
        this.enchantmentMappings.put("PUNCH", (Enchantment) net.minecraft.enchantment.Enchantment.punch);
        this.enchantmentMappings.put("FLAME", (Enchantment) net.minecraft.enchantment.Enchantment.flame);
        this.enchantmentMappings.put("INFINITY", (Enchantment) net.minecraft.enchantment.Enchantment.infinity);
        this.enchantmentMappings.put("LUCK_OF_THE_SEA", (Enchantment) net.minecraft.enchantment.Enchantment.luckOfTheSea);
        this.enchantmentMappings.put("LURE", (Enchantment) net.minecraft.enchantment.Enchantment.lure);

        RegistryHelper.mapFields(Enchantments.class, this.enchantmentMappings);
    }

    // Note: This is probably fairly slow, but only needs to be run rarely.
    private void setPotionTypes() {
        for (Potion potion : Potion.potionTypes) {
            if (potion != null) {
                PotionEffectType potionEffectType = (PotionEffectType) potion;
                this.potionList.add(potionEffectType);
            }
        }
        RegistryHelper.mapFields(PotionEffectTypes.class, new Function<String, PotionEffectType>() {

            @Override
            public PotionEffectType apply(String fieldName) {
                return getPotion(fieldName.toLowerCase()).get();
            }
        });
    }

    private void setBiomeTypes() {
        BiomeGenBase[] biomeArray = BiomeGenBase.getBiomeGenArray();
        for (BiomeGenBase biome : biomeArray) {
            if (biome != null) {
                this.biomeTypes.add((BiomeType) biome);
            }
        }

        this.biomeTypeMappings.put("OCEAN", (BiomeType) BiomeGenBase.ocean);
        this.biomeTypeMappings.put("PLAINS", (BiomeType) BiomeGenBase.plains);
        this.biomeTypeMappings.put("DESERT", (BiomeType) BiomeGenBase.desert);
        this.biomeTypeMappings.put("EXTREME_HILLS", (BiomeType) BiomeGenBase.extremeHills);
        this.biomeTypeMappings.put("FOREST", (BiomeType) BiomeGenBase.forest);
        this.biomeTypeMappings.put("TAIGA", (BiomeType) BiomeGenBase.taiga);
        this.biomeTypeMappings.put("SWAMPLAND", (BiomeType) BiomeGenBase.swampland);
        this.biomeTypeMappings.put("RIVER", (BiomeType) BiomeGenBase.river);
        this.biomeTypeMappings.put("HELL", (BiomeType) BiomeGenBase.hell);
        this.biomeTypeMappings.put("SKY", (BiomeType) BiomeGenBase.sky);
        this.biomeTypeMappings.put("FROZEN_OCEAN", (BiomeType) BiomeGenBase.frozenOcean);
        this.biomeTypeMappings.put("FROZEN_RIVER", (BiomeType) BiomeGenBase.frozenRiver);
        this.biomeTypeMappings.put("ICE_PLAINS", (BiomeType) BiomeGenBase.icePlains);
        this.biomeTypeMappings.put("ICE_MOUNTAINS", (BiomeType) BiomeGenBase.iceMountains);
        this.biomeTypeMappings.put("MUSHROOM_ISLAND", (BiomeType) BiomeGenBase.mushroomIsland);
        this.biomeTypeMappings.put("MUSHROOM_ISLAND_SHORE", (BiomeType) BiomeGenBase.mushroomIslandShore);
        this.biomeTypeMappings.put("BEACH", (BiomeType) BiomeGenBase.beach);
        this.biomeTypeMappings.put("DESERT_HILLS", (BiomeType) BiomeGenBase.desertHills);
        this.biomeTypeMappings.put("FOREST_HILLS", (BiomeType) BiomeGenBase.forestHills);
        this.biomeTypeMappings.put("TAIGA_HILLS", (BiomeType) BiomeGenBase.taigaHills);
        this.biomeTypeMappings.put("EXTREME_HILLS_EDGE", (BiomeType) BiomeGenBase.extremeHillsEdge);
        this.biomeTypeMappings.put("JUNGLE", (BiomeType) BiomeGenBase.jungle);
        this.biomeTypeMappings.put("JUNGLE_HILLS", (BiomeType) BiomeGenBase.jungleHills);
        this.biomeTypeMappings.put("JUNGLE_EDGE", (BiomeType) BiomeGenBase.jungleEdge);
        this.biomeTypeMappings.put("DEEP_OCEAN", (BiomeType) BiomeGenBase.deepOcean);
        this.biomeTypeMappings.put("STONE_BEACH", (BiomeType) BiomeGenBase.stoneBeach);
        this.biomeTypeMappings.put("COLD_BEACH", (BiomeType) BiomeGenBase.coldBeach);
        this.biomeTypeMappings.put("BIRCH_FOREST", (BiomeType) BiomeGenBase.birchForest);
        this.biomeTypeMappings.put("BIRCH_FOREST_HILLS", (BiomeType) BiomeGenBase.birchForestHills);
        this.biomeTypeMappings.put("ROOFED_FOREST", (BiomeType) BiomeGenBase.roofedForest);
        this.biomeTypeMappings.put("COLD_TAIGA", (BiomeType) BiomeGenBase.coldTaiga);
        this.biomeTypeMappings.put("COLD_TAIGA_HILLS", (BiomeType) BiomeGenBase.coldTaigaHills);
        this.biomeTypeMappings.put("MEGA_TAIGA", (BiomeType) BiomeGenBase.megaTaiga);
        this.biomeTypeMappings.put("MEGA_TAIGA_HILLS", (BiomeType) BiomeGenBase.megaTaigaHills);
        this.biomeTypeMappings.put("EXTREME_HILLS_PLUS", (BiomeType) BiomeGenBase.extremeHillsPlus);
        this.biomeTypeMappings.put("SAVANNA", (BiomeType) BiomeGenBase.savanna);
        this.biomeTypeMappings.put("SAVANNA_PLATEAU", (BiomeType) BiomeGenBase.savannaPlateau);
        this.biomeTypeMappings.put("MESA", (BiomeType) BiomeGenBase.mesa);
        this.biomeTypeMappings.put("MESA_PLATEAU_FOREST", (BiomeType) BiomeGenBase.mesaPlateau_F);
        this.biomeTypeMappings.put("MESA_PLATEAU", (BiomeType) BiomeGenBase.mesaPlateau);
        this.biomeTypeMappings.put("SUNFLOWER_PLAINS", (BiomeType) biomeArray[BiomeGenBase.plains.biomeID + 128]);
        this.biomeTypeMappings.put("DESERT_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.desert.biomeID + 128]);
        this.biomeTypeMappings.put("FLOWER_FOREST", (BiomeType) biomeArray[BiomeGenBase.forest.biomeID + 128]);
        this.biomeTypeMappings.put("TAIGA_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.taiga.biomeID + 128]);
        this.biomeTypeMappings.put("SWAMPLAND_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.swampland.biomeID + 128]);
        this.biomeTypeMappings.put("ICE_PLAINS_SPIKES", (BiomeType) biomeArray[BiomeGenBase.icePlains.biomeID + 128]);
        this.biomeTypeMappings.put("JUNGLE_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.jungle.biomeID + 128]);
        this.biomeTypeMappings.put("JUNGLE_EDGE_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.jungleEdge.biomeID + 128]);
        this.biomeTypeMappings.put("COLD_TAIGA_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.coldTaiga.biomeID + 128]);
        this.biomeTypeMappings.put("SAVANNA_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.savanna.biomeID + 128]);
        this.biomeTypeMappings.put("SAVANNA_PLATEAU_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.savannaPlateau.biomeID + 128]);
        this.biomeTypeMappings.put("MESA_BRYCE", (BiomeType) biomeArray[BiomeGenBase.mesa.biomeID + 128]);
        this.biomeTypeMappings.put("MESA_PLATEAU_FOREST_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.mesaPlateau_F.biomeID + 128]);
        this.biomeTypeMappings.put("MESA_PLATEAU_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.mesaPlateau.biomeID + 128]);
        this.biomeTypeMappings.put("BIRCH_FOREST_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.birchForest.biomeID + 128]);
        this.biomeTypeMappings.put("BIRCH_FOREST_HILLS_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.birchForestHills.biomeID + 128]);
        this.biomeTypeMappings.put("ROOFED_FOREST_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.roofedForest.biomeID + 128]);
        this.biomeTypeMappings.put("MEGA_SPRUCE_TAIGA", (BiomeType) biomeArray[BiomeGenBase.megaTaiga.biomeID + 128]);
        this.biomeTypeMappings.put("EXTREME_HILLS_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.extremeHills.biomeID + 128]);
        this.biomeTypeMappings.put("EXTREME_HILLS_PLUS_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.extremeHillsPlus.biomeID + 128]);
        this.biomeTypeMappings.put("MEGA_SPRUCE_TAIGA_HILLS", (BiomeType) biomeArray[BiomeGenBase.megaTaigaHills.biomeID + 128]);

        RegistryHelper.mapFields(BiomeTypes.class, this.biomeTypeMappings);
    }

    private void setCareersAndProfessions() {
        try {
            Professions.class.getDeclaredField("FARMER").set(null, new SpongeProfession(0, "farmer"));
            Careers.class.getDeclaredField("FARMER").set(null, new SpongeCareer(0, "farmer", Professions.FARMER));
            Careers.class.getDeclaredField("FISHERMAN").set(null, new SpongeCareer(1, "fisherman", Professions.FARMER));
            Careers.class.getDeclaredField("SHEPHERD").set(null, new SpongeCareer(2, "shepherd", Professions.FARMER));
            Careers.class.getDeclaredField("FLETCHER").set(null, new SpongeCareer(3, "fletcher", Professions.FARMER));

            Professions.class.getDeclaredField("LIBRARIAN").set(null, new SpongeProfession(1, "librarian"));
            Careers.class.getDeclaredField("LIBRARIAN").set(null, new SpongeCareer(0, "librarian", Professions.LIBRARIAN));

            Professions.class.getDeclaredField("PRIEST").set(null, new SpongeProfession(2, "priest"));
            Careers.class.getDeclaredField("CLERIC").set(null, new SpongeCareer(0, "cleric", Professions.PRIEST));

            Professions.class.getDeclaredField("BLACKSMITH").set(null, new SpongeProfession(3, "blacksmith"));
            Careers.class.getDeclaredField("ARMORER").set(null, new SpongeCareer(0, "armor", Professions.BLACKSMITH));
            Careers.class.getDeclaredField("WEAPON_SMITH").set(null, new SpongeCareer(1, "weapon", Professions.BLACKSMITH));
            Careers.class.getDeclaredField("TOOL_SMITH").set(null, new SpongeCareer(2, "tool", Professions.BLACKSMITH));

            Professions.class.getDeclaredField("BUTCHER").set(null, new SpongeProfession(4, "butcher"));
            Careers.class.getDeclaredField("BUTCHER").set(null, new SpongeCareer(0, "butcher", Professions.BUTCHER));
            Careers.class.getDeclaredField("LEATHERWORKER").set(null, new SpongeCareer(1, "leatherworker", Professions.BUTCHER));

            this.professionMappings.put(Professions.FARMER.getName(), Professions.FARMER);
            this.professionMappings.put(Professions.LIBRARIAN.getName(), Professions.LIBRARIAN);
            this.professionMappings.put(Professions.PRIEST.getName(), Professions.PRIEST);
            this.professionMappings.put(Professions.BLACKSMITH.getName(), Professions.BLACKSMITH);
            this.professionMappings.put(Professions.BUTCHER.getName(), Professions.BUTCHER);
            this.careerMappings.put(Careers.FARMER.getName(), Careers.FARMER);
            this.careerMappings.put(Careers.FISHERMAN.getName(), Careers.FISHERMAN);
            this.careerMappings.put(Careers.SHEPHERD.getName(), Careers.SHEPHERD);
            this.careerMappings.put(Careers.FLETCHER.getName(), Careers.FLETCHER);
            this.careerMappings.put(Careers.LIBRARIAN.getName(), Careers.LIBRARIAN);
            this.careerMappings.put(Careers.CLERIC.getName(), Careers.CLERIC);
            this.careerMappings.put(Careers.ARMORER.getName(), Careers.ARMORER);
            this.careerMappings.put(Careers.WEAPON_SMITH.getName(), Careers.WEAPON_SMITH);
            this.careerMappings.put(Careers.TOOL_SMITH.getName(), Careers.TOOL_SMITH);
            this.careerMappings.put(Careers.BUTCHER.getName(), Careers.BUTCHER);
            this.careerMappings.put(Careers.LEATHERWORKER.getName(), Careers.LEATHERWORKER);
            this.professionToCareerMappings.put(((SpongeEntityMeta) Professions.FARMER).type,
                    Arrays.asList(Careers.FARMER, Careers.FISHERMAN, Careers.SHEPHERD, Careers.FLETCHER));
            this.professionToCareerMappings.put(((SpongeEntityMeta) Professions.LIBRARIAN).type, Arrays.asList(Careers.LIBRARIAN));
            this.professionToCareerMappings.put(((SpongeEntityMeta) Professions.PRIEST).type, Arrays.asList(Careers.CLERIC));
            this.professionToCareerMappings
                    .put(((SpongeEntityMeta) Professions.BLACKSMITH).type, Arrays.asList(Careers.ARMORER, Careers.WEAPON_SMITH, Careers.TOOL_SMITH));
            this.professionToCareerMappings.put(((SpongeEntityMeta) Professions.BUTCHER).type, Arrays.asList(Careers.BUTCHER, Careers.LEATHERWORKER));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTextColor(EnumChatFormatting handle, Color color) {
        SpongeTextColor spongeColor = new SpongeTextColor(handle, color);
        textColorMappings.put(handle.name(), spongeColor);
        enumChatColor.put(handle, spongeColor);
    }

    private void setTextColors() {
        addTextColor(EnumChatFormatting.BLACK, Color.BLACK);
        addTextColor(EnumChatFormatting.DARK_BLUE, new Color(0x0000AA));
        addTextColor(EnumChatFormatting.DARK_GREEN, new Color(0x00AA00));
        addTextColor(EnumChatFormatting.DARK_AQUA, new Color(0x00AAAA));
        addTextColor(EnumChatFormatting.DARK_RED, new Color(0xAA0000));
        addTextColor(EnumChatFormatting.DARK_PURPLE, new Color(0xAA00AA));
        addTextColor(EnumChatFormatting.GOLD, new Color(0xFFAA00));
        addTextColor(EnumChatFormatting.GRAY, new Color(0xAAAAAA));
        addTextColor(EnumChatFormatting.DARK_GRAY, new Color(0x555555));
        addTextColor(EnumChatFormatting.BLUE, new Color(0x5555FF));
        addTextColor(EnumChatFormatting.GREEN, new Color(0x55FF55));
        addTextColor(EnumChatFormatting.AQUA, new Color(0x00FFFF));
        addTextColor(EnumChatFormatting.RED, new Color(0xFF5555));
        addTextColor(EnumChatFormatting.LIGHT_PURPLE, new Color(0xFF55FF));
        addTextColor(EnumChatFormatting.YELLOW, new Color(0xFFFF55));
        addTextColor(EnumChatFormatting.WHITE, Color.WHITE);
        addTextColor(EnumChatFormatting.RESET, Color.WHITE);

        RegistryHelper.mapFields(TextColors.class, textColorMappings);
        RegistryHelper.mapFields(ChatTypes.class, chatTypeMappings);
        RegistryHelper.mapFields(TextStyles.class, textStyleMappings);
    }

    private void setCoal() {
        // Because Minecraft doesn't have any enum stuff for this....
        this.coaltypeMappings.put("COAL", new SpongeCoalType(0, "COAL"));
        this.coaltypeMappings.put("CHARCOAL", new SpongeCoalType(1, "CHARCOAL"));
        RegistryHelper.mapFields(CoalTypes.class, this.coaltypeMappings);
    }

    private void setRotations() {
        RegistryHelper.mapFields(Rotations.class, rotationMappings);
    }

    private void setWeathers() {
        RegistryHelper.mapFields(Weathers.class, new Function<String, Weather>() {

            @Override
            public Weather apply(String fieldName) {
                return new SpongeWeather(fieldName);
            }
        });
    }

    private void setTextActionFactory() {
        //RegistryHelper.setFactory(TextActions.class, new SpongeTextActionFactory());
    }

    private void setTextFactory() {
        RegistryHelper.setFactory(Texts.class, new SpongeTextFactory());
    }

    private void setSelectors() {
        /*
         * try { SelectorTypes.class.getDeclaredField("ALL_PLAYERS").set(null,
         * new SpongeSelectorType("a"));
         * SelectorTypes.class.getDeclaredField("ALL_ENTITIES").set(null, new
         * SpongeSelectorType("e"));
         * SelectorTypes.class.getDeclaredField("NEAREST_PLAYER").set(null, new
         * SpongeSelectorType("p"));
         * SelectorTypes.class.getDeclaredField("RANDOM_PLAYER").set(null, new
         * SpongeSelectorType("r")); } catch (Exception e) {
         * e.printStackTrace(); } RegistryHelper.setFactory(SelectorTypes.class,
         * new SpongeSelectorTypeFactory());
         * RegistryHelper.setFactory(Selectors.class, new
         * SpongeSelectorFactory());
         */
    }

    private void setTitleFactory() {
        //RegistryHelper.setFactory(Titles.class, new SpongeTitleFactory());
    }

    private void setDimensionTypes() {
        try {
            DimensionTypes.class.getDeclaredField("NETHER").set(null, new SpongeDimensionType("NETHER", true, WorldProviderHell.class, -1));
            DimensionTypes.class.getDeclaredField("OVERWORLD").set(null, new SpongeDimensionType("OVERWORLD", true, WorldProviderSurface.class, 0));
            DimensionTypes.class.getDeclaredField("END").set(null, new SpongeDimensionType("END", false, WorldProviderEnd.class, 1));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void setGameModes() {
        RegistryHelper.mapFields(GameModes.class, gameModeMappings);
    }

    private void setSounds() {
        final Map<String, String> soundMappings = Maps.newHashMap();
        soundMappings.put("AMBIENCE_CAVE", "ambient.cave.cave");
        soundMappings.put("AMBIENCE_RAIN", "ambient.weather.rain");
        soundMappings.put("AMBIENCE_THUNDER", "ambient.weather.thunder");
        soundMappings.put("ANVIL_BREAK", "random.anvil_break");
        soundMappings.put("ANVIL_LAND", "random.anvil_land");
        soundMappings.put("ANVIL_USE", "random.anvil_use");
        soundMappings.put("ARROW_HIT", "random.bowhit");
        soundMappings.put("BURP", "random.burp");
        soundMappings.put("CHEST_CLOSE", "random.chestclosed");
        soundMappings.put("CHEST_OPEN", "random.chestopen");
        soundMappings.put("CLICK", "random.click");
        soundMappings.put("DOOR_CLOSE", "random.door_close");
        soundMappings.put("DOOR_OPEN", "random.door_open");
        soundMappings.put("DRINK", "random.drink");
        soundMappings.put("EAT", "random.eat");
        soundMappings.put("EXPLODE", "random.explode");
        soundMappings.put("FALL_BIG", "game.player.hurt.fall.big");
        soundMappings.put("FALL_SMALL", "game.player.hurt.fall.small");
        soundMappings.put("FIRE", "fire.fire");
        soundMappings.put("FIRE_IGNITE", "fire.ignite");
        soundMappings.put("FIRECHARGE_USE", "item.fireCharge.use");
        soundMappings.put("FIZZ", "random.fizz");
        soundMappings.put("FUSE", "game.tnt.primed");
        soundMappings.put("GLASS", "dig.glass");
        soundMappings.put("GUI_BUTTON", "gui.button.press");
        soundMappings.put("HURT_FLESH", "game.player.hurt");
        soundMappings.put("ITEM_BREAK", "random.break");
        soundMappings.put("ITEM_PICKUP", "random.pop");
        soundMappings.put("LAVA", "liquid.lava");
        soundMappings.put("LAVA_POP", "liquid.lavapop");
        soundMappings.put("LEVEL_UP", "random.levelup");
        soundMappings.put("MINECART_BASE", "minecart.base");
        soundMappings.put("MINECART_INSIDE", "minecart.inside");
        soundMappings.put("MUSIC_GAME", "music.game");
        soundMappings.put("MUSIC_CREATIVE", "music.game.creative");
        soundMappings.put("MUSIC_END", "music.game.end");
        soundMappings.put("MUSIC_CREDITS", "music.game.end.credits");
        soundMappings.put("MUSIC_DRAGON", "music.game.end.dragon");
        soundMappings.put("MUSIC_NETHER", "music.game.nether");
        soundMappings.put("MUSIC_MENU", "music.menu");
        soundMappings.put("NOTE_BASS", "note.bass");
        soundMappings.put("NOTE_PIANO", "note.harp");
        soundMappings.put("NOTE_BASS_DRUM", "note.bd");
        soundMappings.put("NOTE_STICKS", "note.hat");
        soundMappings.put("NOTE_BASS_GUITAR", "note.bassattack");
        soundMappings.put("NOTE_SNARE_DRUM", "note.snare");
        soundMappings.put("NOTE_PLING", "note.pling");
        soundMappings.put("ORB_PICKUP", "random.orb");
        soundMappings.put("PISTON_EXTEND", "tile.piston.out");
        soundMappings.put("PISTON_RETRACT", "tile.piston.in");
        soundMappings.put("PORTAL", "portal.portal");
        soundMappings.put("PORTAL_TRAVEL", "portal.travel");
        soundMappings.put("PORTAL_TRIGGER", "portal.trigger");
        soundMappings.put("POTION_SMASH", "game.potion.smash");
        soundMappings.put("RECORDS_11", "records.11");
        soundMappings.put("RECORDS_13", "records.13");
        soundMappings.put("RECORDS_BLOCKS", "records.blocks");
        soundMappings.put("RECORDS_CAT", "records.cat");
        soundMappings.put("RECORDS_CHIRP", "records.chirp");
        soundMappings.put("RECORDS_FAR", "records.far");
        soundMappings.put("RECORDS_MALL", "records.mall");
        soundMappings.put("RECORDS_MELLOHI", "records.mellohi");
        soundMappings.put("RECORDS_STAL", "records.stal");
        soundMappings.put("RECORDS_STRAD", "records.strad");
        soundMappings.put("RECORDS_WAIT", "records.wait");
        soundMappings.put("RECORDS_WARD", "records.ward");
        soundMappings.put("SHOOT_ARROW", "random.bow");
        soundMappings.put("SPLASH", "random.splash");
        soundMappings.put("SPLASH2", "game.player.swim.splash");
        soundMappings.put("STEP_GRASS", "step.grass");
        soundMappings.put("STEP_GRAVEL", "step.gravel");
        soundMappings.put("STEP_LADDER", "step.ladder");
        soundMappings.put("STEP_SAND", "step.sand");
        soundMappings.put("STEP_SNOW", "step.snow");
        soundMappings.put("STEP_STONE", "step.stone");
        soundMappings.put("STEP_WOOD", "step.wood");
        soundMappings.put("STEP_WOOL", "step.cloth");
        soundMappings.put("SWIM", "game.player.swim");
        soundMappings.put("WATER", "liquid.water");
        soundMappings.put("WOOD_CLICK", "random.wood_click");
        soundMappings.put("BAT_DEATH", "mob.bat.death");
        soundMappings.put("BAT_HURT", "mob.bat.hurt");
        soundMappings.put("BAT_IDLE", "mob.bat.idle");
        soundMappings.put("BAT_LOOP", "mob.bat.loop");
        soundMappings.put("BAT_TAKEOFF", "mob.bat.takeoff");
        soundMappings.put("BLAZE_BREATH", "mob.blaze.breathe");
        soundMappings.put("BLAZE_DEATH", "mob.blaze.death");
        soundMappings.put("BLAZE_HIT", "mob.blaze.hit");
        soundMappings.put("CAT_HISS", "mob.cat.hiss");
        soundMappings.put("CAT_HIT", "mob.cat.hitt");
        soundMappings.put("CAT_MEOW", "mob.cat.meow");
        soundMappings.put("CAT_PURR", "mob.cat.purr");
        soundMappings.put("CAT_PURREOW", "mob.cat.purreow");
        soundMappings.put("CHICKEN_IDLE", "mob.chicken.say");
        soundMappings.put("CHICKEN_HURT", "mob.chicken.hurt");
        soundMappings.put("CHICKEN_EGG_POP", "mob.chicken.plop");
        soundMappings.put("CHICKEN_WALK", "mob.chicken.step");
        soundMappings.put("COW_IDLE", "mob.cow.say");
        soundMappings.put("COW_HURT", "mob.cow.hurt");
        soundMappings.put("COW_WALK", "mob.cow.step");
        soundMappings.put("CREEPER_HISS", "creeper.primed");
        soundMappings.put("CREEPER_HIT", "mob.creeper.say");
        soundMappings.put("CREEPER_DEATH", "mob.creeper.death");
        soundMappings.put("ENDERDRAGON_DEATH", "mob.enderdragon.end");
        soundMappings.put("ENDERDRAGON_GROWL", "mob.enderdragon.growl");
        soundMappings.put("ENDERDRAGON_HIT", "mob.enderdragon.hit");
        soundMappings.put("ENDERDRAGON_WINGS", "mob.enderdragon.wings");
        soundMappings.put("ENDERMAN_DEATH", "mob.endermen.death");
        soundMappings.put("ENDERMAN_HIT", "mob.endermen.hit");
        soundMappings.put("ENDERMAN_IDLE", "mob.endermen.idle");
        soundMappings.put("ENDERMAN_TELEPORT", "mob.endermen.portal");
        soundMappings.put("ENDERMAN_SCREAM", "mob.endermen.scream");
        soundMappings.put("ENDERMAN_STARE", "mob.endermen.stare");
        soundMappings.put("GHAST_SCREAM", "mob.ghast.scream");
        soundMappings.put("GHAST_SCREAM2", "mob.ghast.affectionate_scream");
        soundMappings.put("GHAST_CHARGE", "mob.ghast.charge");
        soundMappings.put("GHAST_DEATH", "mob.ghast.death");
        soundMappings.put("GHAST_FIREBALL", "mob.ghast.fireball");
        soundMappings.put("GHAST_MOAN", "mob.ghast.moan");
        soundMappings.put("GUARDIAN_IDLE", "mob.guardian.idle");
        soundMappings.put("GUARDIAN_ATTACK", "mob.guardian.attack");
        soundMappings.put("GUARDIAN_CURSE", "mob.guardian.curse");
        soundMappings.put("GUARDIAN_FLOP", "mob.guardian.flop");
        soundMappings.put("GUARDIAN_ELDER_IDLE", "mob.guardian.elder.idle");
        soundMappings.put("GUARDIAN_LAND_IDLE", "mob.guardian.land.idle");
        soundMappings.put("GUARDIAN_HIT", "mob.guardian.hit");
        soundMappings.put("GUARDIAN_ELDER_HIT", "mob.guardian.elder.hit");
        soundMappings.put("GUARDIAN_LAND_HIT", "mob.guardian.land.hit");
        soundMappings.put("GUARDIAN_DEATH", "mob.guardian.death");
        soundMappings.put("GUARDIAN_ELDER_DEATH", "mob.guardian.elder.death");
        soundMappings.put("GUARDIAN_LAND_DEATH", "mob.guardian.land.death");
        soundMappings.put("HOSTILE_DEATH", "game.hostile.die");
        soundMappings.put("HOSTILE_HURT", "game.hostile.hurt");
        soundMappings.put("HOSTILE_FALL_BIG", "game.hostile.hurt.fall.big");
        soundMappings.put("HOSTILE_FALL_SMALL", "game.hostile.hurt.fall.small");
        soundMappings.put("HOSTILE_SWIM", "game.hostile.swim");
        soundMappings.put("HOSTILE_SPLASH", "game.hostile.swim.splash");
        soundMappings.put("IRONGOLEM_DEATH", "mob.irongolem.death");
        soundMappings.put("IRONGOLEM_HIT", "mob.irongolem.hit");
        soundMappings.put("IRONGOLEM_THROW", "mob.irongolem.throw");
        soundMappings.put("IRONGOLEM_WALK", "mob.irongolem.walk");
        soundMappings.put("MAGMACUBE_WALK", "mob.magmacube.big");
        soundMappings.put("MAGMACUBE_WALK2", "mob.magmacube.small");
        soundMappings.put("MAGMACUBE_JUMP", "mob.magmacube.jump");
        soundMappings.put("NEUTRAL_DEATH", "game.neutral.die");
        soundMappings.put("NEUTRAL_HURT", "game.neutral.hurt");
        soundMappings.put("NEUTRAL_FALL_BIG", "game.neutral.hurt.fall.big");
        soundMappings.put("NEUTRAL_FALL_SMALL", "game.neutral.hurt.fall.small");
        soundMappings.put("NEUTRAL_SWIM", "game.neutral.swim");
        soundMappings.put("NEUTRAL_SPLASH", "game.neutral.swim.splash");
        soundMappings.put("PIG_IDLE", "mob.pig.say");
        soundMappings.put("PIG_DEATH", "mob.pig.death");
        soundMappings.put("PIG_WALK", "mob.pig.step");
        soundMappings.put("PLAYER_DEATH", "game.player.die");
        soundMappings.put("RABBIT_IDLE", "mob.rabbit.idle");
        soundMappings.put("RABBIT_HURT", "mob.rabbit.hurt");
        soundMappings.put("RABBIT_HOP", "mob.rabbit.hop");
        soundMappings.put("RABBIT_DEATH", "mob.rabbit.death");
        soundMappings.put("SHEEP_IDLE", "mob.sheep.say");
        soundMappings.put("SHEEP_SHEAR", "mob.sheep.shear");
        soundMappings.put("SHEEP_WALK", "mob.sheep.step");
        soundMappings.put("SILVERFISH_HIT", "mob.silverfish.hit");
        soundMappings.put("SILVERFISH_DEATH", "mob.silverfish.kill");
        soundMappings.put("SILVERFISH_IDLE", "mob.silverfish.say");
        soundMappings.put("SILVERFISH_WALK", "mob.silverfish.step");
        soundMappings.put("SKELETON_IDLE", "mob.skeleton.say");
        soundMappings.put("SKELETON_DEATH", "mob.skeleton.death");
        soundMappings.put("SKELETON_HURT", "mob.skeleton.hurt");
        soundMappings.put("SKELETON_WALK", "mob.skeleton.step");
        soundMappings.put("SLIME_ATTACK", "mob.slime.attack");
        soundMappings.put("SLIME_WALK", "mob.slime.big");
        soundMappings.put("SLIME_WALK2", "mob.slime.small");
        soundMappings.put("SPIDER_IDLE", "mob.spider.say");
        soundMappings.put("SPIDER_DEATH", "mob.spider.death");
        soundMappings.put("SPIDER_WALK", "mob.spider.step");
        soundMappings.put("WITHER_DEATH", "mob.wither.death");
        soundMappings.put("WITHER_HURT", "mob.wither.hurt");
        soundMappings.put("WITHER_IDLE", "mob.wither.idle");
        soundMappings.put("WITHER_SHOOT", "mob.wither.shoot");
        soundMappings.put("WITHER_SPAWN", "mob.wither.spawn");
        soundMappings.put("WOLF_BARK", "mob.wolf.bark");
        soundMappings.put("WOLF_DEATH", "mob.wolf.death");
        soundMappings.put("WOLF_GROWL", "mob.wolf.growl");
        soundMappings.put("WOLF_HOWL", "mob.wolf.howl");
        soundMappings.put("WOLF_HURT", "mob.wolf.hurt");
        soundMappings.put("WOLF_PANT", "mob.wolf.panting");
        soundMappings.put("WOLF_SHAKE", "mob.wolf.shake");
        soundMappings.put("WOLF_WALK", "mob.wolf.step");
        soundMappings.put("WOLF_WHINE", "mob.wolf.whine");
        soundMappings.put("ZOMBIE_METAL", "mob.zombie.metal");
        soundMappings.put("ZOMBIE_WOOD", "mob.zombie.wood");
        soundMappings.put("ZOMBIE_WOODBREAK", "mob.zombie.woodbreak");
        soundMappings.put("ZOMBIE_IDLE", "mob.zombie.say");
        soundMappings.put("ZOMBIE_DEATH", "mob.zombie.death");
        soundMappings.put("ZOMBIE_HURT", "mob.zombie.hurt");
        soundMappings.put("ZOMBIE_INFECT", "mob.zombie.infect");
        soundMappings.put("ZOMBIE_UNFECT", "mob.zombie.unfect");
        soundMappings.put("ZOMBIE_REMEDY", "mob.zombie.remedy");
        soundMappings.put("ZOMBIE_WALK", "mob.zombie.step");
        soundMappings.put("ZOMBIE_PIG_IDLE", "mob.zombiepig.zpig");
        soundMappings.put("ZOMBIE_PIG_ANGRY", "mob.zombiepig.zpigangry");
        soundMappings.put("ZOMBIE_PIG_DEATH", "mob.zombiepig.zpigdeath");
        soundMappings.put("ZOMBIE_PIG_HURT", "mob.zombiepig.zpighurt");
        soundMappings.put("DIG_WOOL", "dig.cloth");
        soundMappings.put("DIG_GRASS", "dig.grass");
        soundMappings.put("DIG_GRAVEL", "dig.gravel");
        soundMappings.put("DIG_SAND", "dig.sand");
        soundMappings.put("DIG_SNOW", "dig.snow");
        soundMappings.put("DIG_STONE", "dig.stone");
        soundMappings.put("DIG_WOOD", "dig.wood");
        soundMappings.put("FIREWORK_BLAST", "fireworks.blast");
        soundMappings.put("FIREWORK_BLAST2", "fireworks.blast_far");
        soundMappings.put("FIREWORK_LARGE_BLAST", "fireworks.largeBlast");
        soundMappings.put("FIREWORK_LARGE_BLAST2", "fireworks.largeBlast_far");
        soundMappings.put("FIREWORK_TWINKLE", "fireworks.twinkle");
        soundMappings.put("FIREWORK_TWINKLE2", "fireworks.twinkle_far");
        soundMappings.put("FIREWORK_LAUNCH", "fireworks.launch");
        soundMappings.put("SUCCESSFUL_HIT", "random.successful_hit");
        soundMappings.put("HORSE_ANGRY", "mob.horse.angry");
        soundMappings.put("HORSE_ARMOR", "mob.horse.armor");
        soundMappings.put("HORSE_BREATHE", "mob.horse.breathe");
        soundMappings.put("HORSE_DEATH", "mob.horse.death");
        soundMappings.put("HORSE_GALLOP", "mob.horse.gallop");
        soundMappings.put("HORSE_HIT", "mob.horse.hit");
        soundMappings.put("HORSE_IDLE", "mob.horse.idle");
        soundMappings.put("HORSE_JUMP", "mob.horse.jump");
        soundMappings.put("HORSE_LAND", "mob.horse.land");
        soundMappings.put("HORSE_SADDLE", "mob.horse.leather");
        soundMappings.put("HORSE_SOFT", "mob.horse.soft");
        soundMappings.put("HORSE_WOOD", "mob.horse.wood");
        soundMappings.put("DONKEY_ANGRY", "mob.horse.donkey.angry");
        soundMappings.put("DONKEY_DEATH", "mob.horse.donkey.death");
        soundMappings.put("DONKEY_HIT", "mob.horse.donkey.hit");
        soundMappings.put("DONKEY_IDLE", "mob.horse.donkey.idle");
        soundMappings.put("HORSE_SKELETON_DEATH", "mob.horse.skeleton.death");
        soundMappings.put("HORSE_SKELETON_HIT", "mob.horse.skeleton.hit");
        soundMappings.put("HORSE_SKELETON_IDLE", "mob.horse.skeleton.idle");
        soundMappings.put("HORSE_ZOMBIE_DEATH", "mob.horse.zombie.death");
        soundMappings.put("HORSE_ZOMBIE_HIT", "mob.horse.zombie.hit");
        soundMappings.put("HORSE_ZOMBIE_IDLE", "mob.horse.zombie.idle");
        soundMappings.put("VILLAGER_DEATH", "mob.villager.death");
        soundMappings.put("VILLAGER_HAGGLE", "mob.villager.haggle");
        soundMappings.put("VILLAGER_HIT", "mob.villager.hit");
        soundMappings.put("VILLAGER_IDLE", "mob.villager.idle");
        soundMappings.put("VILLAGER_NO", "mob.villager.no");
        soundMappings.put("VILLAGER_YES", "mob.villager.yes");

        RegistryHelper.mapFields(SoundTypes.class, new Function<String, SoundType>() {

            @Override
            public SoundType apply(String fieldName) {
                String soundName = soundMappings.get(fieldName);
                SoundType sound = new SpongeSound(soundName);
                SpongeGameRegistry.this.soundNames.put(soundName, sound);
                return sound;
            }
        });
    }

    private void setDifficulties() {
        RegistryHelper.mapFields(Difficulties.class, difficultyMappings);
    }

    @SuppressWarnings("unchecked")
    private void setupSerialization() {
        Game game = Sponge.getGame();
        SerializationService service = game.getServiceManager().provide(SerializationService.class).get();
        SpongeManipulatorRegistry dataRegistry = SpongeManipulatorRegistry.getInstance();
        // TileEntities
        service.registerBuilder(Banner.class, new SpongeBannerBuilder(game));
        service.registerBuilder(BannerData.PatternLayer.class, new SpongePatternLayerBuilder(game));
        service.registerBuilder(BrewingStand.class, new SpongeBrewingStandBuilder(game));
        service.registerBuilder(Chest.class, new SpongeChestBuilder(game));
        service.registerBuilder(CommandBlock.class, new SpongeCommandBlockBuilder(game));
        service.registerBuilder(org.spongepowered.api.block.tile.Comparator.class, new SpongeComparatorBuilder(game));
        service.registerBuilder(DaylightDetector.class, new SpongeDaylightBuilder(game));
        service.registerBuilder(Dispenser.class, new SpongeDispenserBuilder(game));
        service.registerBuilder(Dropper.class, new SpongeDropperBuilder(game));
        service.registerBuilder(EnchantmentTable.class, new SpongeEnchantmentTableBuilder(game));
        service.registerBuilder(EnderChest.class, new SpongeEnderChestBuilder(game));
        service.registerBuilder(EndPortal.class, new SpongeEndPortalBuilder(game));
        service.registerBuilder(Furnace.class, new SpongeFurnaceBuilder(game));
        service.registerBuilder(Hopper.class, new SpongeHopperBuilder(game));
        service.registerBuilder(MobSpawner.class, new SpongeMobSpawnerBuilder(game));
        service.registerBuilder(Note.class, new SpongeNoteBuilder(game));
        service.registerBuilder(Sign.class, new SpongeSignBuilder(game));
        service.registerBuilder(Skull.class, new SpongeSkullBuilder(game));

        SpongeBannerDataBuilder bannerDataBuilder = new SpongeBannerDataBuilder();
        service.registerBuilder(BannerData.class, bannerDataBuilder);
        dataRegistry.register(BannerData.class, bannerDataBuilder);
        dataRegistry.register((Class<BannerData>) (Class) SpongeBannerData.class, bannerDataBuilder);

        SpongeSignDataBuilder signBuilder = new SpongeSignDataBuilder();
        service.registerBuilder(SignData.class, signBuilder);
        dataRegistry.register(SignData.class, signBuilder);
        dataRegistry.registerDataUtil(SignData.class, signBuilder);
        dataRegistry.registerDataUtil((Class<SignData>) (Class) SpongeSignData.class, signBuilder);

        SpongeBeaconDataBuilder beaconDataBuilder = new SpongeBeaconDataBuilder();
        service.registerBuilder(BeaconData.class, beaconDataBuilder);
        dataRegistry.register(BeaconData.class, beaconDataBuilder);
        dataRegistry.registerDataUtil(BeaconData.class, beaconDataBuilder);
        dataRegistry.registerDataUtil((Class<BeaconData>) (Class) SpongeBeaconData.class, beaconDataBuilder);

        SpongeTradeOfferUtil tradeOfferUtil = new SpongeTradeOfferUtil();
        service.registerBuilder(TradeOfferData.class, tradeOfferUtil);
        dataRegistry.register(TradeOfferData.class, tradeOfferUtil);
        dataRegistry.registerDataUtil(TradeOfferData.class, tradeOfferUtil);
        dataRegistry.registerDataUtil((Class<TradeOfferData>) (Class) SpongeTradeOfferData.class, tradeOfferUtil);

        SpongeLoreUtil loreUtil = new SpongeLoreUtil();
        service.registerBuilder(LoreData.class, loreUtil);
        dataRegistry.register(LoreData.class, loreUtil);
        dataRegistry.registerDataUtil(LoreData.class, loreUtil);
        dataRegistry.registerDataUtil((Class<LoreData>) (Class) SpongeLoreData.class, loreUtil);

        SpongeEnchantmentUtil enchantmentUtil = new SpongeEnchantmentUtil();
        service.registerBuilder(EnchantmentData.class, enchantmentUtil);
        dataRegistry.register(EnchantmentData.class, enchantmentUtil);
        dataRegistry.registerDataUtil(EnchantmentData.class, enchantmentUtil);
        dataRegistry.registerDataUtil((Class<EnchantmentData>) (Class) SpongeEnchantmentItemData.class, enchantmentUtil);

        SpongeDisplayNameDataBuilder displayNameDataBuilder = new SpongeDisplayNameDataBuilder();
        service.registerBuilder(DisplayNameData.class, displayNameDataBuilder);
        dataRegistry.register(DisplayNameData.class, displayNameDataBuilder);
        dataRegistry.registerDataUtil(DisplayNameData.class, displayNameDataBuilder);
        dataRegistry.registerDataUtil((Class<DisplayNameData>) (Class) SpongeDisplayNameData.class, displayNameDataBuilder);

        SpongeCareerDataUtil careerDataUtil = new SpongeCareerDataUtil();
        service.registerBuilder(CareerData.class, careerDataUtil);
        dataRegistry.register(CareerData.class, careerDataUtil);
        dataRegistry.registerDataUtil(CareerData.class, careerDataUtil);
        dataRegistry.registerDataUtil((Class<CareerData>) (Class) SpongeCareerData.class, careerDataUtil);

        SpongeDirectionalUtil directionalUtil = new SpongeDirectionalUtil();
        service.registerBuilder(DirectionalData.class, directionalUtil);
        dataRegistry.register(DirectionalData.class, directionalUtil);
        dataRegistry.registerDataUtil(DirectionalData.class, directionalUtil);
        dataRegistry.registerDataUtil((Class<DirectionalData>) (Class) SpongeDirectionalData.class, directionalUtil);
        dataRegistry.registerBlockUtil(DirectionalData.class, directionalUtil);
        dataRegistry.registerBlockUtil((Class<DirectionalData>) (Class) SpongeDirectionalData.class, directionalUtil);

        SpongePoweredUtil poweredUtil = new SpongePoweredUtil();
        service.registerBuilder(PoweredData.class, poweredUtil);
        dataRegistry.register(PoweredData.class, poweredUtil);
        dataRegistry.registerDataUtil(PoweredData.class, poweredUtil);
        dataRegistry.registerDataUtil((Class<PoweredData>) (Class) SpongePoweredData.class, poweredUtil);
        dataRegistry.registerBlockUtil(PoweredData.class, poweredUtil);
        dataRegistry.registerBlockUtil((Class<PoweredData>) (Class) SpongePoweredData.class, poweredUtil);

        SpongeInvulnerabilityUtil invulnerabilityUtil = new SpongeInvulnerabilityUtil();
        service.registerBuilder(InvulnerabilityData.class, invulnerabilityUtil);
        dataRegistry.register(InvulnerabilityData.class, invulnerabilityUtil);
        dataRegistry.registerDataUtil(InvulnerabilityData.class, invulnerabilityUtil);
        dataRegistry.registerDataUtil((Class<InvulnerabilityData>) (Class) SpongeInvulnerabilityData.class, invulnerabilityUtil);

        SpongeRepresentedItemBuilder representedItemBuilder = new SpongeRepresentedItemBuilder();
        service.registerBuilder(RepresentedItemData.class, representedItemBuilder);
        dataRegistry.register(RepresentedItemData.class, representedItemBuilder);
        dataRegistry.registerDataUtil(RepresentedItemData.class, representedItemBuilder);
        dataRegistry.registerDataUtil((Class<RepresentedItemData>) (Class) SpongeRepresentedItemData.class, representedItemBuilder);

        SpongePotionDataBuilder potionDataBuilder = new SpongePotionDataBuilder();
        service.registerBuilder(PotionEffectData.class, potionDataBuilder);
        dataRegistry.register(PotionEffectData.class, potionDataBuilder);
        dataRegistry.registerDataUtil(PotionEffectData.class, potionDataBuilder);
        dataRegistry.registerDataUtil((Class<PotionEffectData>) (Class) SpongePotionEffectData.class, potionDataBuilder);

        SpongeTradeOfferBuilder tradeOfferBuilder = new SpongeTradeOfferBuilder();
        service.registerBuilder(TradeOffer.class, tradeOfferBuilder);
        // User
        // TODO someone needs to write a User implementation...
    }

    private void setNotePitches() {
        RegistryHelper.mapFields(NotePitches.class, new Function<String, NotePitch>() {

            @Override
            public NotePitch apply(String input) {
                NotePitch pitch = new SpongeNotePitch((byte) SpongeGameRegistry.this.notePitchMappings.size(), input);
                SpongeGameRegistry.this.notePitchMappings.put(input, pitch);
                return pitch;
            }

        });
    }

    private void setSkullTypes() {
        RegistryHelper.mapFields(SkullTypes.class, new Function<String, SkullType>() {

            @Override
            public SkullType apply(String input) {
                SkullType skullType = new SpongeSkullType((byte) SpongeGameRegistry.this.skullTypeMappings.size(), input);
                SpongeGameRegistry.this.skullTypeMappings.put(input, skullType);
                return skullType;
            }

        });
    }

    private void setBannerPatternShapes() {
        RegistryHelper.mapFields(BannerPatternShapes.class, new Function<String, BannerPatternShape>() {

            @Override
            public BannerPatternShape apply(String input) {
                BannerPatternShape bannerPattern = (BannerPatternShape) (Object) TileEntityBanner.EnumBannerPattern.valueOf(input);
                SpongeGameRegistry.this.bannerPatternShapeMappings.put(bannerPattern.getName(), bannerPattern);
                SpongeGameRegistry.this.idToBannerPatternShapeMappings.put(bannerPattern.getId(), bannerPattern);
                return bannerPattern;
            }

        });
    }

    private void setFishes() {
        RegistryHelper.mapFields(Fishes.class, new Function<String, Fish>() {

            @Override
            public Fish apply(String input) {
                Fish fish = (Fish) (Object) ItemFishFood.FishType.valueOf(input);
                if (fish != null) {
                    SpongeGameRegistry.this.fishMappings.put(fish.getId(), fish);
                    return fish;
                } else {
                    return null;
                }
            }
        });

        RegistryHelper.mapFields(CookedFishes.class, new Function<String, CookedFish>() {

            @Override
            public CookedFish apply(String input) {
                CookedFish fish = new SpongeCookedFish(input, input); // TODO
                if (fish != null) {
                    SpongeGameRegistry.this.cookedFishMappings.put(fish.getId(), fish);
                    return fish;
                } else {
                    return null;
                }
            }
        });
    }

    private void setDyeColors() {
        RegistryHelper.mapFields(DyeColors.class, new Function<String, DyeColor>() {

            @Override
            public DyeColor apply(String input) {
                DyeColor dyeColor = (DyeColor) (Object) EnumDyeColor.valueOf(input);
                SpongeGameRegistry.this.dyeColorMappings.put(dyeColor.getName(), dyeColor);
                return dyeColor;
            }

        });
    }

    private void setEntityInteractionTypes() {
        RegistryHelper.mapFields(EntityInteractionTypes.class, SpongeGameRegistry.entityInteractionTypeMappings);
    }

    private void setArts() {
        RegistryHelper.mapFields(Arts.class, new Function<String, Art>() {

            @Override
            public Art apply(String fieldName) {
                Art art = (Art) (Object) EntityPainting.EnumArt.valueOf(fieldName);
                SpongeGameRegistry.this.artMappings.put(art.getName(), art);
                return art;
            }
        });
    }

    private void setEntityTypes() {
        // internal mapping of our EntityTypes to actual MC names
        this.entityTypeMappings.put("DROPPED_ITEM", newEntityTypeFromName("Item"));
        this.entityTypeMappings.put("EXPERIENCE_ORB", newEntityTypeFromName("XPOrb"));
        this.entityTypeMappings.put("LEASH_HITCH", newEntityTypeFromName("LeashKnot"));
        this.entityTypeMappings.put("PAINTING", newEntityTypeFromName("Painting"));
        this.entityTypeMappings.put("ARROW", newEntityTypeFromName("Arrow"));
        this.entityTypeMappings.put("SNOWBALL", newEntityTypeFromName("Snowball"));
        this.entityTypeMappings.put("FIREBALL", newEntityTypeFromName("LargeFireball", "Fireball"));
        this.entityTypeMappings.put("SMALL_FIREBALL", newEntityTypeFromName("SmallFireball"));
        this.entityTypeMappings.put("ENDER_PEARL", newEntityTypeFromName("ThrownEnderpearl"));
        this.entityTypeMappings.put("EYE_OF_ENDER", newEntityTypeFromName("EyeOfEnderSignal"));
        this.entityTypeMappings.put("SPLASH_POTION", newEntityTypeFromName("ThrownPotion"));
        this.entityTypeMappings.put("THROWN_EXP_BOTTLE", newEntityTypeFromName("ThrownExpBottle"));
        this.entityTypeMappings.put("ITEM_FRAME", newEntityTypeFromName("ItemFrame"));
        this.entityTypeMappings.put("WITHER_SKULL", newEntityTypeFromName("WitherSkull"));
        this.entityTypeMappings.put("PRIMED_TNT", newEntityTypeFromName("PrimedTnt"));
        this.entityTypeMappings.put("FALLING_BLOCK", newEntityTypeFromName("FallingSand"));
        this.entityTypeMappings.put("FIREWORK", newEntityTypeFromName("FireworksRocketEntity"));
        this.entityTypeMappings.put("ARMOR_STAND", newEntityTypeFromName("ArmorStand"));
        this.entityTypeMappings.put("BOAT", newEntityTypeFromName("Boat"));
        this.entityTypeMappings.put("RIDEABLE_MINECART", newEntityTypeFromName("MinecartRideable"));
        this.entityTypeMappings.put("CHESTED_MINECART", newEntityTypeFromName("MinecartChest"));
        this.entityTypeMappings.put("FURNACE_MINECART", newEntityTypeFromName("MinecartFurnace"));
        this.entityTypeMappings.put("TNT_MINECART", newEntityTypeFromName("MinecartTnt", "MinecartTNT"));
        this.entityTypeMappings.put("HOPPER_MINECART", newEntityTypeFromName("MinecartHopper"));
        this.entityTypeMappings.put("MOB_SPAWNER_MINECART", newEntityTypeFromName("MinecartSpawner"));
        this.entityTypeMappings.put("COMMANDBLOCK_MINECART", newEntityTypeFromName("MinecartCommandBlock"));
        this.entityTypeMappings.put("CREEPER", newEntityTypeFromName("Creeper"));
        this.entityTypeMappings.put("SKELETON", newEntityTypeFromName("Skeleton"));
        this.entityTypeMappings.put("SPIDER", newEntityTypeFromName("Spider"));
        this.entityTypeMappings.put("GIANT", newEntityTypeFromName("Giant"));
        this.entityTypeMappings.put("ZOMBIE", newEntityTypeFromName("Zombie"));
        this.entityTypeMappings.put("SLIME", newEntityTypeFromName("Slime"));
        this.entityTypeMappings.put("GHAST", newEntityTypeFromName("Ghast"));
        this.entityTypeMappings.put("PIG_ZOMBIE", newEntityTypeFromName("PigZombie"));
        this.entityTypeMappings.put("ENDERMAN", newEntityTypeFromName("Enderman"));
        this.entityTypeMappings.put("CAVE_SPIDER", newEntityTypeFromName("CaveSpider"));
        this.entityTypeMappings.put("SILVERFISH", newEntityTypeFromName("Silverfish"));
        this.entityTypeMappings.put("BLAZE", newEntityTypeFromName("Blaze"));
        this.entityTypeMappings.put("MAGMA_CUBE", newEntityTypeFromName("LavaSlime"));
        this.entityTypeMappings.put("ENDER_DRAGON", newEntityTypeFromName("EnderDragon"));
        this.entityTypeMappings.put("WITHER", newEntityTypeFromName("WitherBoss"));
        this.entityTypeMappings.put("BAT", newEntityTypeFromName("Bat"));
        this.entityTypeMappings.put("WITCH", newEntityTypeFromName("Witch"));
        this.entityTypeMappings.put("ENDERMITE", newEntityTypeFromName("Endermite"));
        this.entityTypeMappings.put("GUARDIAN", newEntityTypeFromName("Guardian"));
        this.entityTypeMappings.put("PIG", newEntityTypeFromName("Pig"));
        this.entityTypeMappings.put("SHEEP", newEntityTypeFromName("Sheep"));
        this.entityTypeMappings.put("COW", newEntityTypeFromName("Cow"));
        this.entityTypeMappings.put("CHICKEN", newEntityTypeFromName("Chicken"));
        this.entityTypeMappings.put("SQUID", newEntityTypeFromName("Squid"));
        this.entityTypeMappings.put("WOLF", newEntityTypeFromName("Wolf"));
        this.entityTypeMappings.put("MUSHROOM_COW", newEntityTypeFromName("MushroomCow"));
        this.entityTypeMappings.put("SNOWMAN", newEntityTypeFromName("SnowMan"));
        this.entityTypeMappings.put("OCELOT", newEntityTypeFromName("Ozelot"));
        this.entityTypeMappings.put("IRON_GOLEM", newEntityTypeFromName("VillagerGolem"));
        this.entityTypeMappings.put("HORSE", newEntityTypeFromName("EntityHorse"));
        this.entityTypeMappings.put("RABBIT", newEntityTypeFromName("Rabbit"));
        this.entityTypeMappings.put("VILLAGER", newEntityTypeFromName("Villager"));
        this.entityTypeMappings.put("ENDER_CRYSTAL", newEntityTypeFromName("EnderCrystal"));
        this.entityTypeMappings.put("EGG", new SpongeEntityType(-1, "Egg", EntityEgg.class));
        this.entityTypeMappings.put("FISHING_HOOK", new SpongeEntityType(-2, "FishingHook", EntityFishHook.class));
        this.entityTypeMappings.put("LIGHTNING", new SpongeEntityType(-3, "Lightning", EntityLightningBolt.class));
        this.entityTypeMappings.put("WEATHER", new SpongeEntityType(-4, "Weather", EntityWeatherEffect.class));
        this.entityTypeMappings.put("PLAYER", new SpongeEntityType(-5, "Player", EntityPlayerMP.class));
        this.entityTypeMappings.put("COMPLEX_PART", new SpongeEntityType(-6, "ComplexPart", EntityDragonPart.class));

        RegistryHelper.mapFields(EntityTypes.class, new Function<String, EntityType>() {

            @Override
            public EntityType apply(String fieldName) {
                if (fieldName.equals("UNKNOWN")) {
                    // TODO Something for Unknown?
                    return null;
                }
                EntityType entityType = SpongeGameRegistry.this.entityTypeMappings.get(fieldName);
                SpongeGameRegistry.this.entityClassToTypeMappings
                        .put(((SpongeEntityType) entityType).entityClass, (SpongeEntityType) entityType);
                SpongeGameRegistry.this.entityIdToTypeMappings.put(((SpongeEntityType) entityType).getId(), ((SpongeEntityType) entityType));
                return entityType;
            }
        });

        RegistryHelper.mapFields(SkeletonTypes.class, SpongeEntityConstants.SKELETON_TYPES);
        RegistryHelper.mapFields(HorseColors.class, SpongeEntityConstants.HORSE_COLORS);
        RegistryHelper.mapFields(HorseVariants.class, SpongeEntityConstants.HORSE_VARIANTS);
        RegistryHelper.mapFields(HorseStyles.class, SpongeEntityConstants.HORSE_STYLES);
        RegistryHelper.mapFields(OcelotTypes.class, SpongeEntityConstants.OCELOT_TYPES);
        RegistryHelper.mapFields(RabbitTypes.class, SpongeEntityConstants.RABBIT_TYPES);
    }


    private SpongeEntityType newEntityTypeFromName(String spongeName, String mcName) {
        return new SpongeEntityType((Integer) EntityList.stringToIDMapping.get(mcName), spongeName,
                (Class<? extends Entity>) EntityList.stringToClassMapping.get(mcName));
    }

    private SpongeEntityType newEntityTypeFromName(String name) {
        return newEntityTypeFromName(name, name);
    }

    @Override
    public com.google.common.base.Optional<EntityStatistic> getEntityStatistic(StatisticGroup statisticGroup, EntityType entityType) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public com.google.common.base.Optional<ItemStatistic> getItemStatistic(StatisticGroup statisticGroup, ItemType itemType) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public com.google.common.base.Optional<BlockStatistic> getBlockStatistic(StatisticGroup statisticGroup, BlockType blockType) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public com.google.common.base.Optional<TeamStatistic> getTeamStatistic(StatisticGroup statisticGroup, TextColor teamColor) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public Collection<Statistic> getStatistics(StatisticGroup statisticGroup) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public void registerStatistic(Statistic stat) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public com.google.common.base.Optional<ResourcePack> getById(String id) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public com.google.common.base.Optional<DisplaySlot> getDisplaySlotForColor(TextColor color) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public PopulatorFactory getPopulatorFactory() {
        throw new UnsupportedOperationException(); // TODO
    }

    public void preInit() {
        setupSerialization();
    }

    public void init() {
        setDimensionTypes();
        setEnchantments();
        setCareersAndProfessions();
        setTextColors();
        setRotations();
        setWeathers();
        setTextActionFactory();
        setTextFactory();
        setSelectors();
        setTitleFactory();
        setParticles();
        setGameModes();
        setSounds();
        setDifficulties();
        setArts();
        setDyeColors();
        setSkullTypes();
        setNotePitches();
        setBannerPatternShapes();
        setEntityInteractionTypes();
    }

    public void postInit() {
        setPotionTypes();
        setBiomeTypes();
        setCoal();
        setFishes();
        setEntityTypes();
    }
}
