package snow.geiger.registry;

import net.minecraft.util.Identifier;

import java.util.Locale;

import static snow.geiger.reflect.Reflection.*;

public interface Registrable {
	// Iterate all fields in the container and accept public + static + final fields
	// that are able to be casted to Registrable.
	// To get the identifier path, either use the value of the @Path annotation if present or the lowercased field name.
	// After which, register the value statically acquired from the field.
	static void register(Class<?> container, String namespace) {
		iterateConstants(container, field ->
			staticSafeCast(field, Registrable.class, o -> {
				final String path = annotationOrDefault(field, Path.class,
					Path::value,
					() -> field.getName().toLowerCase(Locale.ROOT)
				);
				o.register(new Identifier(namespace, path));
			})
		);
	}

	void register(Identifier id);
}