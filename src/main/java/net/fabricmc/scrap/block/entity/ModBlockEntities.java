package net.fabricmc.scrap.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.scrap.Main;
import net.fabricmc.scrap.block.ModBlocks;
import net.fabricmc.scrap.block.entity.ducts.energyducts.ConductiveEnergyDuctEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import team.reborn.energy.api.base.SimpleEnergyStorage;


public class ModBlockEntities {

    public static BlockEntityType<ConductiveEnergyDuctEntity> CONDUCTIVE_ENERGY_DUCT_ENTITY;
    public static BlockEntityType<OreWasherEntity> ORE_WASHER_ENTITY;
    public static BlockEntityType<FurnaceGeneratorEntity> FURNACE_GENERATOR_ENTITY;
    public static BlockEntityType<SmelterEntity> SMELTER_ENTITY;
    public static BlockEntityType<CrusherEntity> CRUSHER_ENTITY;
    public static BlockEntityType<AlloySmelterEntity> ALLOY_SMELTER_ENTITY;
    public static BlockEntityType<BlockBreakerEntity> BLOCK_BREAKER_ENTITY;
    public static BlockEntityType<BlockPlacerEntity> BLOCK_PLACER_ENTITY;
    public static BlockEntityType<ChargerEntity> CHARGER_ENTITY;
    public static BlockEntityType<QuarryEntity> QUARRY_ENTITY;

    public static void registerBlockEntities() {
        //Ore Washer
        ORE_WASHER_ENTITY =
                Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "ore_washer_block"),
                        FabricBlockEntityTypeBuilder.create(OreWasherEntity::new,
                                ModBlocks.ORE_WASHER_BLOCK).build(null));


        CONDUCTIVE_ENERGY_DUCT_ENTITY =
                Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "conductive_energy_duct_block"),
                        FabricBlockEntityTypeBuilder.create(ConductiveEnergyDuctEntity::new,
                                ModBlocks.CONDUCTIVE_ENERGY_DUCT_BLOCK).build(null));

        //Furnace Generator
        FURNACE_GENERATOR_ENTITY =
                Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "furnace_generator_block"),
                        FabricBlockEntityTypeBuilder.create(FurnaceGeneratorEntity::new,
                                ModBlocks.FURNACE_GENERATOR_BLOCK).build(null));

        SimpleEnergyStorage.SIDED.registerForBlockEntity((myBlockEntity, direction) -> myBlockEntity.energyStorage, FURNACE_GENERATOR_ENTITY);

        //Smelter
        SMELTER_ENTITY=Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "smelter_block"),
                        FabricBlockEntityTypeBuilder.create(SmelterEntity::new,
                                ModBlocks.SMELTER_BLOCK).build(null));

        SimpleEnergyStorage.SIDED.registerForBlockEntity((myBlockEntity, direction) -> myBlockEntity.energyStorage, SMELTER_ENTITY);

        //Crusher
        CRUSHER_ENTITY=Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "crusher_block"),
                        FabricBlockEntityTypeBuilder.create(CrusherEntity::new,
                                ModBlocks.CRUSHER_BLOCK).build(null));

        SimpleEnergyStorage.SIDED.registerForBlockEntity((myBlockEntity, direction) -> myBlockEntity.energyStorage, CRUSHER_ENTITY);

        //Alloy smelter
        ALLOY_SMELTER_ENTITY =Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "alloy_smelter_block"),
                        FabricBlockEntityTypeBuilder.create(AlloySmelterEntity::new,
                                ModBlocks.ALLOY_SMELTER_BLOCK).build(null));

        SimpleEnergyStorage.SIDED.registerForBlockEntity((myBlockEntity, direction) -> myBlockEntity.energyStorage, ALLOY_SMELTER_ENTITY);

        //BlockBreaker
        BLOCK_BREAKER_ENTITY =Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "block_breaker_block"),
                FabricBlockEntityTypeBuilder.create(BlockBreakerEntity::new,
                        ModBlocks.BLOCK_BREAKER_BLOCK).build(null));

        SimpleEnergyStorage.SIDED.registerForBlockEntity((myBlockEntity, direction) -> myBlockEntity.energyStorage, BLOCK_BREAKER_ENTITY);

        //BlockPlacer
        BLOCK_PLACER_ENTITY =Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "block_placer_block"),
                FabricBlockEntityTypeBuilder.create(BlockPlacerEntity::new,
                        ModBlocks.BLOCK_PLACER_BLOCK).build(null));

        SimpleEnergyStorage.SIDED.registerForBlockEntity((myBlockEntity, direction) -> myBlockEntity.energyStorage, BLOCK_PLACER_ENTITY);

        //Charger
        CHARGER_ENTITY =Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "charger_block"),
                FabricBlockEntityTypeBuilder.create(ChargerEntity::new,
                        ModBlocks.CHARGER_BLOCK).build(null));

        SimpleEnergyStorage.SIDED.registerForBlockEntity((myBlockEntity, direction) -> myBlockEntity.energyStorage, CHARGER_ENTITY);

        //Quarry
        QUARRY_ENTITY =Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "quarry_block"),
                FabricBlockEntityTypeBuilder.create(QuarryEntity::new,
                        ModBlocks.QUARRY_BLOCK).build(null));

        SimpleEnergyStorage.SIDED.registerForBlockEntity((myBlockEntity, direction) -> myBlockEntity.energyStorage, QUARRY_ENTITY);
    }
}
