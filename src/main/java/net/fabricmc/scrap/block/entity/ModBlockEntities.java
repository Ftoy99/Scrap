package net.fabricmc.scrap.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.scrap.Main;
import net.fabricmc.scrap.block.ModBlocks;
import net.fabricmc.scrap.block.entity.ducts.energyducts.ConductiveEnergyDuctBlockEntity;
import net.fabricmc.scrap.block.entity.ducts.energyducts.EnergyDuctBlockEntity;
import net.fabricmc.scrap.block.entity.ducts.energyducts.EnergyNetwork;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import team.reborn.energy.api.base.SimpleEnergyStorage;


public class ModBlockEntities {


    public static BlockEntityType<EnergyDuctBlockEntity> ENERGY_DUCT_BLOCK_ENTITY;
    public static BlockEntityType<OreWasherBlockEntity> ORE_WASHER_BLOCK_ENTITY;
    public static BlockEntityType<FurnaceGeneratorBlockEntity> FURNACE_GENERATOR_BLOCK_ENTITY;
    public static BlockEntityType<SmelterBlockEntity> SMELTER_BLOCK_ENTITY;
    public static BlockEntityType<CrusherBlockEntity> CRUSHER_BLOCK_ENTITY;
    public static BlockEntityType<AlloySmelterBlockEntity> ALLOY_SMELTER_BLOCK_ENTITY;
    public static BlockEntityType<ConductiveEnergyDuctBlockEntity> CONDUCTIVE_ENERGY_DUCT_BLOCK_ENTITY;

    public static void registerBlockEntities() {
        //Ore Washer
        ORE_WASHER_BLOCK_ENTITY =
                Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "ore_washer_block"),
                        FabricBlockEntityTypeBuilder.create(OreWasherBlockEntity::new,
                                ModBlocks.ORE_WASHER_BLOCK).build(null));

        ENERGY_DUCT_BLOCK_ENTITY =
                Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "energy_duct_block"),
                        FabricBlockEntityTypeBuilder.create(EnergyDuctBlockEntity::new,
                                ModBlocks.ENERGY_DUCT_BLOCK).build(null));
        SimpleEnergyStorage.SIDED.registerForBlockEntity((myBlockEntity, direction) -> myBlockEntity.energyStorage, ENERGY_DUCT_BLOCK_ENTITY);

        //Furnace Generator
        FURNACE_GENERATOR_BLOCK_ENTITY =
                Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "furnace_generator_block"),
                        FabricBlockEntityTypeBuilder.create(FurnaceGeneratorBlockEntity::new,
                                ModBlocks.FURNACE_GENERATOR_BLOCK).build(null));

        SimpleEnergyStorage.SIDED.registerForBlockEntity((myBlockEntity, direction) -> myBlockEntity.energyStorage, FURNACE_GENERATOR_BLOCK_ENTITY);

        //Smelter
        SMELTER_BLOCK_ENTITY=Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "smelter_block"),
                        FabricBlockEntityTypeBuilder.create(SmelterBlockEntity::new,
                                ModBlocks.SMELTER_BLOCK).build(null));

        SimpleEnergyStorage.SIDED.registerForBlockEntity((myBlockEntity, direction) -> myBlockEntity.energyStorage, SMELTER_BLOCK_ENTITY);

        //Crusher
        CRUSHER_BLOCK_ENTITY=Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "crusher_block"),
                        FabricBlockEntityTypeBuilder.create(CrusherBlockEntity::new,
                                ModBlocks.CRUSHER_BLOCK).build(null));

        SimpleEnergyStorage.SIDED.registerForBlockEntity((myBlockEntity, direction) -> myBlockEntity.energyStorage, CRUSHER_BLOCK_ENTITY);

        //Alloy smelter
        ALLOY_SMELTER_BLOCK_ENTITY=Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "alloy_smelter_block"),
                        FabricBlockEntityTypeBuilder.create(AlloySmelterBlockEntity::new,
                                ModBlocks.ALLOY_SMELTER_BLOCK).build(null));

        SimpleEnergyStorage.SIDED.registerForBlockEntity((myBlockEntity, direction) -> myBlockEntity.energyStorage, ALLOY_SMELTER_BLOCK_ENTITY);

        CONDUCTIVE_ENERGY_DUCT_BLOCK_ENTITY=Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "conductive_energy_duct_block"),
                FabricBlockEntityTypeBuilder.create(ConductiveEnergyDuctBlockEntity::new,
                        ModBlocks.CONDUCTIVE_ENERGY_DUCT_BLOCK).build(null));

    }
}
