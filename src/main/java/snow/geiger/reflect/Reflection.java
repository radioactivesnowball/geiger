package snow.geiger.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Reflection {
	public static final int CONST = Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL;

	private Reflection() {}

	public static void iterateConstants(Class<?> container, Consumer<Field> body) {
		for (Field field : container.getFields()) {
			if ((field.getModifiers() & CONST) == CONST) body.accept(field);
		}
	}

	public static boolean is(Field field, Class<?> type) {
		return type.isAssignableFrom(field.getType());
	}

	public static <T> void staticSafeCast(Field field, Class<T> type, Consumer<T> consumer) {
		safeCast(field, null, type, consumer);
	}

	public static <T> void safeCast(Field field, Object instance, Class<T> type, Consumer<T> consumer) {
		if (is(field, type)) {
			final T value;
			try {
				value = type.cast(field.get(instance));
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			consumer.accept(value);
		}
	}

	public static <A extends Annotation, R> R annotationOrDefault(AnnotatedElement element, Class<A> annotation,
	                                                              Function<A, R> ifPresent, Supplier<R> ifAbsent) {
		if (element.isAnnotationPresent(annotation)) {
			return ifPresent.apply(element.getAnnotation(annotation));
		} else {
			return ifAbsent.get();
		}
	}
}