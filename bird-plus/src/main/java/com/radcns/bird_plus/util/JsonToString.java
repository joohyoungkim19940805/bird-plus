package com.radcns.bird_plus.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import io.r2dbc.postgresql.codec.Json;

@SuppressWarnings("serial")
public class JsonToString extends StdSerializer<Json>{
	   public JsonToString() {
	        this(null);
	    }

	    public JsonToString(Class<Json> t) {
	        super(t);
	    }

	    @Override
	    public void serialize(Json value, JsonGenerator gen, SerializerProvider arg2) throws IOException {
	        gen.writeString(value.asString());
	    }
}
