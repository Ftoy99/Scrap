package net.fabricmc.scrap.block.entity;

import com.google.common.collect.Maps;
import net.fabricmc.scrap.Main;
import net.fabricmc.scrap.item.inventory.ImplementedInventory;
import net.fabricmc.scrap.screens.FurnaceGeneratorScreenHandler;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.model.CubeFace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.*;

public class FurnaceGeneratorBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(16000, 0, 16000) {
        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };
    protected final PropertyDelegate propertyDelegate;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    int burnTime;
    int fuelTime;
    int generates = 10;

    public FurnaceGeneratorBlockEntity(BlockPos pos, BlockState state) {

        super(ModBlockEntities.FURNACE_GENERATOR_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0:
                        return FurnaceGeneratorBlockEntity.this.burnTime;
                    case 1:
                        return FurnaceGeneratorBlockEntity.this.fuelTime;
                    case 2:
                        return (int) FurnaceGeneratorBlockEntity.this.energyStorage.amount;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        FurnaceGeneratorBlockEntity.this.burnTime = value;
                        break;
                    case 1:
                        FurnaceGeneratorBlockEntity.this.fuelTime = value;
                        break;
                    case 2:
                        FurnaceGeneratorBlockEntity.this.energyStorage.amount = value;
                        break;
                    default:
                        break;
                }
            }

            public int size() {
                return 3;
            }
        };
    }

    public static Map<Item, Integer> createFuelTimeMap() {
        LinkedHashMap<Item, Integer> map = Maps.newLinkedHashMap();
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.COAL_BLOCK, 16000);
        FurnaceGeneratorBlockEntity.addFuel(map, Items.BLAZE_ROD, 2400);
        FurnaceGeneratorBlockEntity.addFuel(map, Items.COAL, 1600);
        FurnaceGeneratorBlockEntity.addFuel(map, Items.CHARCOAL, 1600);
        FurnaceGeneratorBlockEntity.addFuel(map, ItemTags.LOGS, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, ItemTags.PLANKS, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, ItemTags.WOODEN_STAIRS, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, ItemTags.WOODEN_SLABS, 150);
        FurnaceGeneratorBlockEntity.addFuel(map, ItemTags.WOODEN_TRAPDOORS, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, ItemTags.WOODEN_PRESSURE_PLATES, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.OAK_FENCE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.BIRCH_FENCE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.SPRUCE_FENCE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.JUNGLE_FENCE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.DARK_OAK_FENCE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.ACACIA_FENCE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.MANGROVE_FENCE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.OAK_FENCE_GATE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.BIRCH_FENCE_GATE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.SPRUCE_FENCE_GATE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.JUNGLE_FENCE_GATE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.DARK_OAK_FENCE_GATE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.ACACIA_FENCE_GATE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.MANGROVE_FENCE_GATE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.NOTE_BLOCK, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.BOOKSHELF, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.LECTERN, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.JUKEBOX, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.CHEST, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.TRAPPED_CHEST, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.CRAFTING_TABLE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.DAYLIGHT_DETECTOR, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, ItemTags.BANNERS, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Items.BOW, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Items.FISHING_ROD, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.LADDER, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, ItemTags.SIGNS, 200);
        FurnaceGeneratorBlockEntity.addFuel(map, Items.WOODEN_SHOVEL, 200);
        FurnaceGeneratorBlockEntity.addFuel(map, Items.WOODEN_SWORD, 200);
        FurnaceGeneratorBlockEntity.addFuel(map, Items.WOODEN_HOE, 200);
        FurnaceGeneratorBlockEntity.addFuel(map, Items.WOODEN_AXE, 200);
        FurnaceGeneratorBlockEntity.addFuel(map, Items.WOODEN_PICKAXE, 200);
        FurnaceGeneratorBlockEntity.addFuel(map, ItemTags.WOODEN_DOORS, 200);
        FurnaceGeneratorBlockEntity.addFuel(map, ItemTags.BOATS, 1200);
        FurnaceGeneratorBlockEntity.addFuel(map, ItemTags.WOOL, 100);
        FurnaceGeneratorBlockEntity.addFuel(map, ItemTags.WOODEN_BUTTONS, 100);
        FurnaceGeneratorBlockEntity.addFuel(map, Items.STICK, 100);
        FurnaceGeneratorBlockEntity.addFuel(map, ItemTags.SAPLINGS, 100);
        FurnaceGeneratorBlockEntity.addFuel(map, Items.BOWL, 100);
        FurnaceGeneratorBlockEntity.addFuel(map, ItemTags.WOOL_CARPETS, 67);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.DRIED_KELP_BLOCK, 4001);
        FurnaceGeneratorBlockEntity.addFuel(map, Items.CROSSBOW, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.BAMBOO, 50);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.DEAD_BUSH, 100);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.SCAFFOLDING, 50);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.LOOM, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.BARREL, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.CARTOGRAPHY_TABLE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.FLETCHING_TABLE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.SMITHING_TABLE, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.COMPOSTER, 300);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.AZALEA, 100);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.FLOWERING_AZALEA, 100);
        FurnaceGeneratorBlockEntity.addFuel(map, Blocks.MANGROVE_ROOTS, 300);
        return map;
    }

    public static void addFuel(Map<Item, Integer> fuelTimes, TagKey<Item> tag, int fuelTime) {
        for (RegistryEntry<Item> registryEntry : Registry.ITEM.iterateEntries(tag)) {
            if (FurnaceGeneratorBlockEntity.isNonFlammableWood(registryEntry.value())) continue;
            fuelTimes.put(registryEntry.value(), fuelTime);
        }
    }

    public static void addFuel(Map<Item, Integer> fuelTimes, ItemConvertible item, int fuelTime) {
        Item item2 = item.asItem();
        if (FurnaceGeneratorBlockEntity.isNonFlammableWood(item2)) {
            if (SharedConstants.isDevelopment) {
                throw Util.throwOrPause(new IllegalStateException("A developer tried to explicitly make fire resistant item " + item2.getName(null).getString() + " a furnace fuel. That will not work!"));
            }
            return;
        }
        fuelTimes.put(item2, fuelTime);
    }

    public static boolean canUseAsFuel(ItemStack stack) {
        return FurnaceGeneratorBlockEntity.createFuelTimeMap().containsKey(stack.getItem());
    }

    public static void tick(World world, BlockPos pos, BlockState state, FurnaceGeneratorBlockEntity blockEntity) {
        if (blockEntity.isBurning()) {
            --blockEntity.burnTime;
            //If power not full generate power
            if (blockEntity.energyStorage.amount < blockEntity.energyStorage.capacity) {
                if (blockEntity.generates <= blockEntity.energyStorage.capacity - blockEntity.energyStorage.amount) {
                    blockEntity.energyStorage.amount = blockEntity.energyStorage.amount + blockEntity.generates;
                } else {
                    blockEntity.energyStorage.amount = blockEntity.energyStorage.amount + (blockEntity.energyStorage.capacity - blockEntity.energyStorage.amount);
                }

            }
        }

        ItemStack itemStack = blockEntity.inventory.get(0);
        boolean bl3 = !blockEntity.inventory.get(0).isEmpty();
        boolean bl4 = !itemStack.isEmpty();
        if (blockEntity.isBurning() || bl4 && bl3) {
            int i = blockEntity.getMaxCountPerStack();
            if (!blockEntity.isBurning() && !blockEntity.isFull()) {
                blockEntity.fuelTime = blockEntity.burnTime = blockEntity.getFuelTime(itemStack);
                if (blockEntity.isBurning()) {
                    if (bl4) {
                        Item item = itemStack.getItem();
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            Item item2 = item.getRecipeRemainder();
                            blockEntity.inventory.set(0, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
                        }
                    }
                }
            }
        }

//        List<Direction> dirs = new LinkedList<>();
//        dirs.add(Direction.UP);
//        dirs.add(Direction.DOWN);
//        dirs.add(Direction.SOUTH);
//        dirs.add(Direction.EAST);
//        dirs.add(Direction.WEST);
//        dirs.add(Direction.NORTH);
//        for(Direction direction:dirs){
//                pushEnergy(world,pos,blockEntity,direction);
//
//        }

    }
//    public static long pushEnergy(World world,BlockPos pos,FurnaceGeneratorBlockEntity blockEntity,Direction direction){
//        @Nullable
//        EnergyStorage maybeStorage = SimpleEnergyStorage.SIDED.find(world,pos.offset(direction,1),direction);
//        if (maybeStorage == null) {
//            return  0;
//        } else {
//            return EnergyStorageUtil.move(
//                    blockEntity.energyStorage, // from source
//                    maybeStorage, // into target
//                    blockEntity.energyStorage.maxExtract,
//                    null // create a new transaction for this operation
//            );
//        }
//    }
    public static boolean isNonFlammableWood(Item item) {
        return item.getRegistryEntry().isIn(ItemTags.NON_FLAMMABLE_WOOD);
    }

    private boolean isFull() {
        return this.energyStorage.amount >= this.energyStorage.capacity;
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    protected int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        }
        Item item = fuel.getItem();
        return FurnaceGeneratorBlockEntity.createFuelTimeMap().getOrDefault(item, 0);
    }

    public boolean isFuel(ItemStack itemStack) {
        return FurnaceGeneratorBlockEntity.canUseAsFuel(itemStack);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Furnace Generator");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new FurnaceGeneratorScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
        nbt.putInt("furnacegenerator.burnTime", burnTime);
        nbt.putInt("furnacegenerator.fuelTime", fuelTime);
        nbt.putInt("furnacegenerator.amount", (int) energyStorage.amount);
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        burnTime = nbt.getInt("furnacegenerator.burnTime");
        fuelTime = nbt.getInt("furnacegenerator.fuelTime");
        energyStorage.amount = nbt.getInt("furnacegenerator.amount");

    }
}
