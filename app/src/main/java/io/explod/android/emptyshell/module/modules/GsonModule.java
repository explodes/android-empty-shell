package io.explod.android.emptyshell.module.modules;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GsonModule {

	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; // 2015-03-04T14:15:23.039-07:00

	private static final SimpleDateFormat[] DATE_FORMATS = new SimpleDateFormat[]{
		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.Z", Locale.US),    // 2015-03-04T14:15:23-07:00
		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US), // 2015-03-04T14:15:23.039-07:00
		new SimpleDateFormat("yyyy-MM-dd", Locale.US)                  // 2014-03-31
	};

	@Provides
	@Singleton
	public Gson providesGson() {
		return build(FieldNamingPolicy.IDENTITY);
	}

	private Gson build(FieldNamingPolicy fieldNamingPolicy) {
		return new GsonBuilder()
			.registerTypeAdapter(Date.class, new DateFormatter())
			.setFieldNamingPolicy(fieldNamingPolicy)
			.create();
	}


	private static class DateFormatter implements JsonDeserializer<Date>, JsonSerializer<Date> {

		@Override
		public Date deserialize(JsonElement jsonElement, Type typeOfDest, JsonDeserializationContext context) throws JsonParseException {
			for (SimpleDateFormat format : DATE_FORMATS) {
				Date parsed;
				try {
					parsed = format.parse(jsonElement.getAsString());
				} catch (ParseException e) {
					// keep moving forward
					continue;
				}
				return parsed;
			}
			throw new JsonParseException("Could not parse date: \"" + jsonElement.getAsString() + "\".");
		}

		@Override
		public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
			if (src == null) {
				return null;
			}
			String formatted = new SimpleDateFormat(DATE_FORMAT, Locale.US).format(src);
			return new JsonPrimitive(formatted);
		}
	}
}
