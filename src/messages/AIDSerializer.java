package messages;

import java.lang.reflect.Type;

import jade.core.AID;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class AIDSerializer implements JsonSerializer<AID>, JsonDeserializer<AID> {

	@Override
	public AID deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		return new AID(arg0.getAsJsonPrimitive().toString(), true);
	}

	@Override
	public JsonElement serialize(AID arg0, Type arg1,
			JsonSerializationContext arg2) {
		return new JsonPrimitive(arg0.getName());
	}

}
