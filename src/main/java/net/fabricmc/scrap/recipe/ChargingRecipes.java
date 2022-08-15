package net.fabricmc.scrap.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ChargingRecipes implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final int time;
    private final int consumption;
    private final ItemStack output;
    private final DefaultedList<Ingredient> recipeItems;

    public ChargingRecipes(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems,int time,int consumption) {
        this.id = id;
        this.time = time;
        this.consumption = consumption;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient()) {
            return false;
        }
        return recipeItems.get(0).test(inventory.getStack(0));
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
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

    public int getTime() {
        return time;
    }

    public int getConsumption() {
        return consumption;
    }

    public static class Type implements RecipeType<ChargingRecipes> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "charging";

        private Type() {
        }
    }

    public static class Serializer implements RecipeSerializer<ChargingRecipes> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "charging";

        @Override
        public ChargingRecipes read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));
            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(1, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }
            int time = JsonHelper.getInt(json, "time");
            int consumption = JsonHelper.getInt(json, "consumption");
            return new ChargingRecipes(id, output, inputs,time,consumption);
        }

        @Override
        public ChargingRecipes read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            int time = buf.readInt();
            int consumption = buf.readInt();
            return new ChargingRecipes(id, output, inputs,time,consumption);
        }

        @Override
        public void write(PacketByteBuf buf, ChargingRecipes recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
            buf.writeInt(recipe.time);
            buf.writeInt(recipe.consumption);
        }
    }
}
