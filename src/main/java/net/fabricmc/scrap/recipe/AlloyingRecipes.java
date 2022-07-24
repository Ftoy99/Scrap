package net.fabricmc.scrap.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

public class AlloyingRecipes implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final int time;
    private final int consumption;
    private final ItemStack input1;
    private final ItemStack input2;
    private final ItemStack output;


    public AlloyingRecipes(Identifier id, ItemStack input1, ItemStack input2, ItemStack output, int time, int consumption) {
        this.id = id;
        this.time = time;
        this.consumption = consumption;
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        ItemStack invInput1 = inventory.getStack(0);
        ItemStack invInput2 = inventory.getStack(1);
        return (checkStack(input1,invInput1)&&checkStack(input2,invInput2)) || ((checkStack(input1,invInput2)&&checkStack(input2,invInput1)));
    }

    public boolean checkStack(ItemStack recipeInput, ItemStack inventoryInput) {
        return recipeInput.getItem() == inventoryInput.getItem() && inventoryInput.getCount() >= recipeInput.getCount();
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    public ItemStack getInput1() {
        return input1.copy();
    }
    public ItemStack getInput2() {
        return input2.copy();
    }
    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public int getConsumption() {
        return consumption;
    }

    public int getTime() {
        return time;
    }

    public static class Type implements RecipeType<AlloyingRecipes> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "alloying";

        private Type() {
        }
    }

    public static class Serializer implements RecipeSerializer<AlloyingRecipes> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "alloying";

        @Override
        public AlloyingRecipes read(Identifier id, JsonObject json) {
            ItemStack input1 = ScrapRecipeHelper.getItemStack(JsonHelper.getObject(json, "input1"));
            ItemStack input2 = ScrapRecipeHelper.getItemStack(JsonHelper.getObject(json, "input2"));
            ItemStack output = ScrapRecipeHelper.getItemStack(JsonHelper.getObject(json, "output"));
            int time = JsonHelper.getInt(json, "time");
            int consumption = JsonHelper.getInt(json, "consumption");
            return new AlloyingRecipes(id, input1, input2, output, time, consumption);
        }

        @Override
        public AlloyingRecipes read(Identifier id, PacketByteBuf buf) {
            ItemStack input1 = buf.readItemStack();
            ItemStack input2 = buf.readItemStack();
            ItemStack output = buf.readItemStack();
            int time = buf.readInt();
            int consumption = buf.readInt();
            return new AlloyingRecipes(id, input1, input2, output, time, consumption);
        }

        @Override
        public void write(PacketByteBuf buf, AlloyingRecipes recipe) {
            buf.writeItemStack(recipe.input1);
            buf.writeItemStack(recipe.input2);
            buf.writeItemStack(recipe.output);
            buf.writeInt(recipe.time);
            buf.writeInt(recipe.consumption);
        }
    }
}
