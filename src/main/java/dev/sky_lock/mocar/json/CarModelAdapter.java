package dev.sky_lock.mocar.json;

import com.google.gson.*;
import dev.sky_lock.mocar.car.CarItem;
import dev.sky_lock.mocar.car.CarModel;
import org.bukkit.Material;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sky_lock
 */

public class CarModelAdapter implements JsonSerializer<CarModel>, JsonDeserializer<CarModel> {

    @Override
    public CarModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        String id = obj.get("id").getAsString();
        String name = obj.get("name").getAsString();
        float maxfuel = obj.get("maxfuel").getAsFloat();
        int maxspeed = obj.get("maxspeed").getAsInt();
        JsonArray lores = obj.get("lore").getAsJsonArray();
        List<String> modelLore = new ArrayList<>();
        for (int i = 0; i < lores.size(); i++) {
            modelLore.add(lores.get(i).getAsString());
        }
        JsonObject itemObj = obj.get("item").getAsJsonObject();
        Material material = Material.valueOf(itemObj.get("material").getAsString());
        short durability = itemObj.get("durability").getAsShort();
        CarItem carItem = new CarItem(material, durability);
        int capacity = obj.get("capacity").getAsInt();
        return new CarModel(id, carItem, name, modelLore, maxfuel, maxspeed, capacity);
    }

    @Override
    public JsonElement serialize(CarModel carModel, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.add("id", new JsonPrimitive(carModel.getId()));
        obj.add("name", new JsonPrimitive(carModel.getName()));
        obj.add("maxfuel", new JsonPrimitive(carModel.getMaxFuel()));
        obj.add("maxspeed", new JsonPrimitive(carModel.getMaxSpeed()));
        obj.add("capacity", new JsonPrimitive(carModel.getCapacity()));
        JsonArray array = new JsonArray();
        carModel.getLores().forEach(array::add);
        obj.add("lore", array);
        JsonObject itemObj = new JsonObject();
        itemObj.add("material", new JsonPrimitive(carModel.getItem().getType().toString()));
        itemObj.add("durability", new JsonPrimitive(carModel.getItem().getDurability()));
        obj.add("item", itemObj);
        return obj;
    }
}
