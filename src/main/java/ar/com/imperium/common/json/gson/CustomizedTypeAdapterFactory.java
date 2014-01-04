/**
 * Copyright 2013 Agustín Miura <"agustin.miura@gmail.com">
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package ar.com.imperium.common.json.gson;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public abstract class CustomizedTypeAdapterFactory<C> implements
    TypeAdapterFactory
{
    private static final Logger logger = LoggerFactory
        .getLogger(CustomizedTypeAdapterFactory.class);

    private final Class<C> customizedClass;

    public CustomizedTypeAdapterFactory(Class<C> customizedClass)
    {
        this.customizedClass = customizedClass;
    }

    @SuppressWarnings("unchecked")
    // we use a runtime check to guarantee that 'C' and 'T' are equal
    public final <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type)
    {
        TypeAdapter<T> answer =
            type.getRawType() == customizedClass ? (TypeAdapter<T>) customizeMyClassAdapter(
                gson,
                (TypeToken<C>) type) : null;

        return answer;
    }

    private TypeAdapter<C> customizeMyClassAdapter(Gson gson, TypeToken<C> type)
    {
        final TypeAdapter<C> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter =
            gson.getAdapter(JsonElement.class);
        return new TypeAdapter<C>()
        {
            @Override
            public void write(JsonWriter out, C value) throws IOException
            {
                if (value == null) {
                    out.nullValue();
                } else {
                    JsonElement tree = delegate.toJsonTree(value);
                    beforeWrite(value, tree);
                    elementAdapter.write(out, tree);
                }
            }

            @Override
            public C read(JsonReader in) throws IOException
            {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else {
                    JsonElement tree = elementAdapter.read(in);
                    afterRead(tree);
                    return delegate.fromJsonTree(tree);
                }
            }
        };
    }

    /**
     * Override this to muck with {@code toSerialize} before it is written to
     * the outgoing JSON stream.
     */
    protected abstract void beforeWrite(C source, JsonElement toSerialize);

    /**
     * Override this to muck with {@code deserialized} before it parsed into the
     * application type.
     */
    protected abstract void afterRead(JsonElement deserialized);
}
