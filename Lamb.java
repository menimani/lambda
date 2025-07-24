package lambda;

/**
 * {@code Lamb} は、例外をスローするラムダ式を即時実行または保持して実行するためのユーティリティクラスです。
 * <p>
 * 各種 {@code FunctionalInterface} をラップし、引数の有無・戻り値の有無・例外の有無にかかわらず、
 * 統一的な形でラムダ式を扱うことができます。
 * </p>
 *
 * <h2>主な用途</h2>
 * <ul>
 *   <li>{@code da()} メソッド：ラムダ式の即時実行</li>
 *   <li>{@code of()} メソッド：ラムダ式の保持・後から実行</li>
 * </ul>
 *
 * <h2>使用例</h2>
 * <pre>{@code
 * // 引数なし・戻り値あり（即時実行）
 * final String value = Lamb.da(() -> "Hello");
 *
 * // 引数あり・戻り値あり（即時実行）
 * final int doubled = Lamb.da((x) -> x * 2, 21);
 *
 * // ラムダ式の保持と後からの実行
 * final var supplier = Lamb.of(() -> "cached");
 * final String result = supplier.da(); // 任意のタイミングで実行
 *
 * // 引数ありの保持と後からの実行
 * final var func = Lamb.of((x, y) -> x + y);
 * final String joined = func.da("Hello, ", "World");
 * }</pre>
 *
 * <h2>命名規則</h2>
 * <ul>
 *   <li>{@code Runnable} … 引数なし、戻り値なし</li>
 *   <li>{@code Supplier} … 引数なし、戻り値あり</li>
 *   <li>{@code Consumer} … 引数あり、戻り値なし（最大20引数まで）</li>
 *   <li>{@code Function} … 引数あり、戻り値あり（最大20引数まで）</li>
 * </ul>
 *
 * <h2>例外伝搬</h2>
 * <p>
 * 全ての {@code da()} メソッドは、対象のラムダ式が例外をスローした場合、
 * その例外をラップせずにそのまま呼び出し元に伝播させます。
 * </p>
 *
 * <p>
 * Java標準の {@code Runnable} や {@code Function} はチェック例外に対応できませんが、
 * 本クラスは {@code throws} を明示する設計により、安全に例外対応が可能です。
 * </p>
 *
 * @author menimani
 * @since 1.0
 */
public final class Lamb {
	private Lamb() {}
	
	// =========== ThrowableRunnable ===========
	
	/**
	 * ラムダ式（引数なし、戻り値なし）
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableRunnable<E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数なし、戻り値なし）
		 * @throws E ラムダ式がスローした例外
		 */
		void da() throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数なし、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 *
	 * @param action 保持するラムダ式
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <E extends Throwable> ThrowableRunnable<E> of(ThrowableRunnable<E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数なし、戻り値なし）
	 *
	 * @param action 実行するラムダ式
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <E extends Throwable> void da(ThrowableRunnable<E> action) throws E {
		of(action).da();
	}
	
	// =========== ThrowableSupplier ===========
	
	/**
	 * ラムダ式（引数なし、戻り値あり）
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableSupplier<R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数なし、戻り値あり）
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da() throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数なし、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 *
	 * @param action 保持するラムダ式
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <R, E extends Throwable> ThrowableSupplier<R, E> of(ThrowableSupplier<R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数なし、戻り値あり）
	 *
	 * @param action 実行するラムダ式
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <R, E extends Throwable> R da(ThrowableSupplier<R, E> action) throws E {
		return of(action).da();
	}
	
	// =========== ThrowableConsumer ===========
	
	/**
	 * ラムダ式（引数1個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer01<T1, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数1個、戻り値なし）
		 * @param arg1 第1引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数1個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, E extends Throwable> ThrowableConsumer01<T1, E> of(ThrowableConsumer01<T1, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数1個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param <T1> 第1引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, E extends Throwable> void da(ThrowableConsumer01<T1, E> action, T1 arg1) throws E {
		of(action).da(arg1);
	}
	
	/**
	 * ラムダ式（引数2個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer02<T1, T2, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数2個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数2個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, E extends Throwable> ThrowableConsumer02<T1, T2, E> of(ThrowableConsumer02<T1, T2, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数2個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, E extends Throwable> void da(ThrowableConsumer02<T1, T2, E> action, T1 arg1, T2 arg2) throws E {
		of(action).da(arg1, arg2);
	}
	
	/**
	 * ラムダ式（引数3個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer03<T1, T2, T3, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数3個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数3個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, E extends Throwable> ThrowableConsumer03<T1, T2, T3, E> of(ThrowableConsumer03<T1, T2, T3, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数3個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, E extends Throwable> void da(ThrowableConsumer03<T1, T2, T3, E> action, T1 arg1, T2 arg2, T3 arg3) throws E {
		of(action).da(arg1, arg2, arg3);
	}
	
	/**
	 * ラムダ式（引数4個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer04<T1, T2, T3, T4, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数4個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数4個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, E extends Throwable> ThrowableConsumer04<T1, T2, T3, T4, E> of(ThrowableConsumer04<T1, T2, T3, T4, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数4個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, E extends Throwable> void da(ThrowableConsumer04<T1, T2, T3, T4, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4) throws E {
		of(action).da(arg1, arg2, arg3, arg4);
	}
	
	/**
	 * ラムダ式（引数5個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer05<T1, T2, T3, T4, T5, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数5個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数5個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, E extends Throwable> ThrowableConsumer05<T1, T2, T3, T4, T5, E> of(ThrowableConsumer05<T1, T2, T3, T4, T5, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数5個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, E extends Throwable> void da(ThrowableConsumer05<T1, T2, T3, T4, T5, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5);
	}
	
	/**
	 * ラムダ式（引数6個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer06<T1, T2, T3, T4, T5, T6, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数6個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数6個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, E extends Throwable> ThrowableConsumer06<T1, T2, T3, T4, T5, T6, E> of(ThrowableConsumer06<T1, T2, T3, T4, T5, T6, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数6個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, E extends Throwable> void da(ThrowableConsumer06<T1, T2, T3, T4, T5, T6, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5, arg6);
	}
	
	/**
	 * ラムダ式（引数7個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer07<T1, T2, T3, T4, T5, T6, T7, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数7個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数7個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, E extends Throwable> ThrowableConsumer07<T1, T2, T3, T4, T5, T6, T7, E> of(ThrowableConsumer07<T1, T2, T3, T4, T5, T6, T7, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数7個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, E extends Throwable> void da(ThrowableConsumer07<T1, T2, T3, T4, T5, T6, T7, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}
	
	/**
	 * ラムダ式（引数8個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer08<T1, T2, T3, T4, T5, T6, T7, T8, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数8個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数8個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, E extends Throwable> ThrowableConsumer08<T1, T2, T3, T4, T5, T6, T7, T8, E> of(ThrowableConsumer08<T1, T2, T3, T4, T5, T6, T7, T8, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数8個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, E extends Throwable> void da(ThrowableConsumer08<T1, T2, T3, T4, T5, T6, T7, T8, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}
	
	/**
	 * ラムダ式（引数9個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer09<T1, T2, T3, T4, T5, T6, T7, T8, T9, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数9個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数9個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, E extends Throwable> ThrowableConsumer09<T1, T2, T3, T4, T5, T6, T7, T8, T9, E> of(ThrowableConsumer09<T1, T2, T3, T4, T5, T6, T7, T8, T9, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数9個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, E extends Throwable> void da(ThrowableConsumer09<T1, T2, T3, T4, T5, T6, T7, T8, T9, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	}
	
	/**
	 * ラムダ式（引数10個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数10個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数10個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E extends Throwable> ThrowableConsumer10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E> of(ThrowableConsumer10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数10個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E extends Throwable> void da(ThrowableConsumer10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	}
	
	/**
	 * ラムダ式（引数11個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数11個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数11個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E extends Throwable> ThrowableConsumer11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E> of(ThrowableConsumer11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数11個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E extends Throwable> void da(ThrowableConsumer11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
	}
	
	/**
	 * ラムダ式（引数12個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数12個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数12個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E extends Throwable> ThrowableConsumer12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E> of(ThrowableConsumer12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数12個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E extends Throwable> void da(ThrowableConsumer12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
	}
	
	/**
	 * ラムダ式（引数13個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数13個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数13個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E extends Throwable> ThrowableConsumer13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E> of(ThrowableConsumer13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数13個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E extends Throwable> void da(ThrowableConsumer13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
	}
	
	/**
	 * ラムダ式（引数14個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数14個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @param arg14 第14引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数14個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E extends Throwable> ThrowableConsumer14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E> of(ThrowableConsumer14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数14個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param arg14 第14引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E extends Throwable> void da(ThrowableConsumer14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
	}
	
	/**
	 * ラムダ式（引数15個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数15個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @param arg14 第14引数
		 * @param arg15 第15引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数15個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E extends Throwable> ThrowableConsumer15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E> of(ThrowableConsumer15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数15個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param arg14 第14引数
	 * @param arg15 第15引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E extends Throwable> void da(ThrowableConsumer15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
	}
	
	/**
	 * ラムダ式（引数16個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数16個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @param arg14 第14引数
		 * @param arg15 第15引数
		 * @param arg16 第16引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数16個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E extends Throwable> ThrowableConsumer16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E> of(ThrowableConsumer16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数16個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param arg14 第14引数
	 * @param arg15 第15引数
	 * @param arg16 第16引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E extends Throwable> void da(ThrowableConsumer16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
	}
	
	/**
	 * ラムダ式（引数17個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数17個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @param arg14 第14引数
		 * @param arg15 第15引数
		 * @param arg16 第16引数
		 * @param arg17 第17引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数17個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E extends Throwable> ThrowableConsumer17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E> of(ThrowableConsumer17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数17個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param arg14 第14引数
	 * @param arg15 第15引数
	 * @param arg16 第16引数
	 * @param arg17 第17引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E extends Throwable> void da(ThrowableConsumer17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
	}
	
	/**
	 * ラムダ式（引数18個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数18個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @param arg14 第14引数
		 * @param arg15 第15引数
		 * @param arg16 第16引数
		 * @param arg17 第17引数
		 * @param arg18 第18引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17, T18 arg18) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数18個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E extends Throwable> ThrowableConsumer18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E> of(ThrowableConsumer18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数18個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param arg14 第14引数
	 * @param arg15 第15引数
	 * @param arg16 第16引数
	 * @param arg17 第17引数
	 * @param arg18 第18引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E extends Throwable> void da(ThrowableConsumer18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17, T18 arg18) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
	}
	
	/**
	 * ラムダ式（引数19個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <T19> 第19引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数19個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @param arg14 第14引数
		 * @param arg15 第15引数
		 * @param arg16 第16引数
		 * @param arg17 第17引数
		 * @param arg18 第18引数
		 * @param arg19 第19引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17, T18 arg18, T19 arg19) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数19個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <T19> 第19引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E extends Throwable> ThrowableConsumer19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E> of(ThrowableConsumer19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数19個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param arg14 第14引数
	 * @param arg15 第15引数
	 * @param arg16 第16引数
	 * @param arg17 第17引数
	 * @param arg18 第18引数
	 * @param arg19 第19引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <T19> 第19引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E extends Throwable> void da(ThrowableConsumer19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17, T18 arg18, T19 arg19) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
	}
	
	/**
	 * ラムダ式（引数20個、戻り値なし）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <T19> 第19引数の型
	 * @param <T20> 第20引数の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableConsumer20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数20個、戻り値なし）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @param arg14 第14引数
		 * @param arg15 第15引数
		 * @param arg16 第16引数
		 * @param arg17 第17引数
		 * @param arg18 第18引数
		 * @param arg19 第19引数
		 * @param arg20 第20引数
		 * @throws E ラムダ式がスローした例外
		 */
		void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17, T18 arg18, T19 arg19, T20 arg20) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数20個、戻り値なし）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <T19> 第19引数の型
	 * @param <T20> 第20引数の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E extends Throwable> ThrowableConsumer20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E> of(ThrowableConsumer20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数20個、戻り値なし）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param arg14 第14引数
	 * @param arg15 第15引数
	 * @param arg16 第16引数
	 * @param arg17 第17引数
	 * @param arg18 第18引数
	 * @param arg19 第19引数
	 * @param arg20 第20引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <T19> 第19引数の型
	 * @param <T20> 第20引数の型
	 * @param <E> スローされる例外の型
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E extends Throwable> void da(ThrowableConsumer20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17, T18 arg18, T19 arg19, T20 arg20) throws E {
		of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
	}
	
	// =========== ThrowableFunction ===========
	
	/**
	 * ラムダ式（引数1個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction01<T1, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数1個、戻り値あり）
		 * @param arg1 第1引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数1個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, R, E extends Throwable> ThrowableFunction01<T1, R, E> of(ThrowableFunction01<T1, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数1個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param <T1> 第1引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, R, E extends Throwable> R da(ThrowableFunction01<T1, R, E> action, T1 arg1) throws E {
		return of(action).da(arg1);
	}
	
	/**
	 * ラムダ式（引数2個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction02<T1, T2, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数2個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数2個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, R, E extends Throwable> ThrowableFunction02<T1, T2, R, E> of(ThrowableFunction02<T1, T2, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数2個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, R, E extends Throwable> R da(ThrowableFunction02<T1, T2, R, E> action, T1 arg1, T2 arg2) throws E {
		return of(action).da(arg1, arg2);
	}
	
	/**
	 * ラムダ式（引数3個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction03<T1, T2, T3, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数3個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数3個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, R, E extends Throwable> ThrowableFunction03<T1, T2, T3, R, E> of(ThrowableFunction03<T1, T2, T3, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数3個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, R, E extends Throwable> R da(ThrowableFunction03<T1, T2, T3, R, E> action, T1 arg1, T2 arg2, T3 arg3) throws E {
		return of(action).da(arg1, arg2, arg3);
	}
	
	/**
	 * ラムダ式（引数4個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction04<T1, T2, T3, T4, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数4個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数4個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, R, E extends Throwable> ThrowableFunction04<T1, T2, T3, T4, R, E> of(ThrowableFunction04<T1, T2, T3, T4, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数4個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, R, E extends Throwable> R da(ThrowableFunction04<T1, T2, T3, T4, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4) throws E {
		return of(action).da(arg1, arg2, arg3, arg4);
	}
	
	/**
	 * ラムダ式（引数5個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction05<T1, T2, T3, T4, T5, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数5個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数5個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, R, E extends Throwable> ThrowableFunction05<T1, T2, T3, T4, T5, R, E> of(ThrowableFunction05<T1, T2, T3, T4, T5, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数5個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, R, E extends Throwable> R da(ThrowableFunction05<T1, T2, T3, T4, T5, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5);
	}
	
	/**
	 * ラムダ式（引数6個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction06<T1, T2, T3, T4, T5, T6, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数6個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数6個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, R, E extends Throwable> ThrowableFunction06<T1, T2, T3, T4, T5, T6, R, E> of(ThrowableFunction06<T1, T2, T3, T4, T5, T6, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数6個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, R, E extends Throwable> R da(ThrowableFunction06<T1, T2, T3, T4, T5, T6, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5, arg6);
	}
	
	/**
	 * ラムダ式（引数7個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction07<T1, T2, T3, T4, T5, T6, T7, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数7個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数7個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, R, E extends Throwable> ThrowableFunction07<T1, T2, T3, T4, T5, T6, T7, R, E> of(ThrowableFunction07<T1, T2, T3, T4, T5, T6, T7, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数7個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, R, E extends Throwable> R da(ThrowableFunction07<T1, T2, T3, T4, T5, T6, T7, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}
	
	/**
	 * ラムダ式（引数8個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction08<T1, T2, T3, T4, T5, T6, T7, T8, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数8個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数8個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, R, E extends Throwable> ThrowableFunction08<T1, T2, T3, T4, T5, T6, T7, T8, R, E> of(ThrowableFunction08<T1, T2, T3, T4, T5, T6, T7, T8, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数8個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, R, E extends Throwable> R da(ThrowableFunction08<T1, T2, T3, T4, T5, T6, T7, T8, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}
	
	/**
	 * ラムダ式（引数9個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction09<T1, T2, T3, T4, T5, T6, T7, T8, T9, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数9個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数9個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R, E extends Throwable> ThrowableFunction09<T1, T2, T3, T4, T5, T6, T7, T8, T9, R, E> of(ThrowableFunction09<T1, T2, T3, T4, T5, T6, T7, T8, T9, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数9個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R, E extends Throwable> R da(ThrowableFunction09<T1, T2, T3, T4, T5, T6, T7, T8, T9, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	}
	
	/**
	 * ラムダ式（引数10個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数10個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数10個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R, E extends Throwable> ThrowableFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R, E> of(ThrowableFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数10個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R, E extends Throwable> R da(ThrowableFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	}
	
	/**
	 * ラムダ式（引数11個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数11個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数11個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R, E extends Throwable> ThrowableFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R, E> of(ThrowableFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数11個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R, E extends Throwable> R da(ThrowableFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
	}
	
	/**
	 * ラムダ式（引数12個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数12個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数12個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R, E extends Throwable> ThrowableFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R, E> of(ThrowableFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数12個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R, E extends Throwable> R da(ThrowableFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
	}
	
	/**
	 * ラムダ式（引数13個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数13個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数13個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R, E extends Throwable> ThrowableFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R, E> of(ThrowableFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数13個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R, E extends Throwable> R da(ThrowableFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
	}
	
	/**
	 * ラムダ式（引数14個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数14個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @param arg14 第14引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数14個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R, E extends Throwable> ThrowableFunction14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R, E> of(ThrowableFunction14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数14個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param arg14 第14引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R, E extends Throwable> R da(ThrowableFunction14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
	}
	
	/**
	 * ラムダ式（引数15個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数15個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @param arg14 第14引数
		 * @param arg15 第15引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数15個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R, E extends Throwable> ThrowableFunction15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R, E> of(ThrowableFunction15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数15個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param arg14 第14引数
	 * @param arg15 第15引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R, E extends Throwable> R da(ThrowableFunction15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
	}
	
	/**
	 * ラムダ式（引数16個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数16個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @param arg14 第14引数
		 * @param arg15 第15引数
		 * @param arg16 第16引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数16個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R, E extends Throwable> ThrowableFunction16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R, E> of(ThrowableFunction16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数16個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param arg14 第14引数
	 * @param arg15 第15引数
	 * @param arg16 第16引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R, E extends Throwable> R da(ThrowableFunction16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
	}
	
	/**
	 * ラムダ式（引数17個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数17個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @param arg14 第14引数
		 * @param arg15 第15引数
		 * @param arg16 第16引数
		 * @param arg17 第17引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数17個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R, E extends Throwable> ThrowableFunction17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R, E> of(ThrowableFunction17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数17個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param arg14 第14引数
	 * @param arg15 第15引数
	 * @param arg16 第16引数
	 * @param arg17 第17引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R, E extends Throwable> R da(ThrowableFunction17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
	}
	
	/**
	 * ラムダ式（引数18個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数18個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @param arg14 第14引数
		 * @param arg15 第15引数
		 * @param arg16 第16引数
		 * @param arg17 第17引数
		 * @param arg18 第18引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17, T18 arg18) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数18個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R, E extends Throwable> ThrowableFunction18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R, E> of(ThrowableFunction18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数18個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param arg14 第14引数
	 * @param arg15 第15引数
	 * @param arg16 第16引数
	 * @param arg17 第17引数
	 * @param arg18 第18引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R, E extends Throwable> R da(ThrowableFunction18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17, T18 arg18) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
	}
	
	/**
	 * ラムダ式（引数19個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <T19> 第19引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数19個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @param arg14 第14引数
		 * @param arg15 第15引数
		 * @param arg16 第16引数
		 * @param arg17 第17引数
		 * @param arg18 第18引数
		 * @param arg19 第19引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17, T18 arg18, T19 arg19) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数19個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <T19> 第19引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R, E extends Throwable> ThrowableFunction19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R, E> of(ThrowableFunction19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数19個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param arg14 第14引数
	 * @param arg15 第15引数
	 * @param arg16 第16引数
	 * @param arg17 第17引数
	 * @param arg18 第18引数
	 * @param arg19 第19引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <T19> 第19引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R, E extends Throwable> R da(ThrowableFunction19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17, T18 arg18, T19 arg19) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
	}
	
	/**
	 * ラムダ式（引数20個、戻り値あり）
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <T19> 第19引数の型
	 * @param <T20> 第20引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 */
	@FunctionalInterface
	public interface ThrowableFunction20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R, E extends Throwable> {
		/**
		 * ラムダ式を即時実行します。（引数20個、戻り値あり）
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @param arg3 第3引数
		 * @param arg4 第4引数
		 * @param arg5 第5引数
		 * @param arg6 第6引数
		 * @param arg7 第7引数
		 * @param arg8 第8引数
		 * @param arg9 第9引数
		 * @param arg10 第10引数
		 * @param arg11 第11引数
		 * @param arg12 第12引数
		 * @param arg13 第13引数
		 * @param arg14 第14引数
		 * @param arg15 第15引数
		 * @param arg16 第16引数
		 * @param arg17 第17引数
		 * @param arg18 第18引数
		 * @param arg19 第19引数
		 * @param arg20 第20引数
		 * @return ラムダ式の戻り値
		 * @throws E ラムダ式がスローした例外
		 */
		R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17, T18 arg18, T19 arg19, T20 arg20) throws E;
	}
	
	/**
	 * ラムダ式を保持します。（引数20個、戻り値あり）
	 * <p>
	 * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
	 * @param action 保持するラムダ式
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <T19> 第19引数の型
	 * @param <T20> 第20引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return 保持されたラムダ式
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R, E extends Throwable> ThrowableFunction20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R, E> of(ThrowableFunction20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R, E> action) {
		return action;
	}
	
	/**
	 * ラムダ式を即時実行します。（引数20個、戻り値あり）
	 * @param action 実行するラムダ式
	 * @param arg1 第1引数
	 * @param arg2 第2引数
	 * @param arg3 第3引数
	 * @param arg4 第4引数
	 * @param arg5 第5引数
	 * @param arg6 第6引数
	 * @param arg7 第7引数
	 * @param arg8 第8引数
	 * @param arg9 第9引数
	 * @param arg10 第10引数
	 * @param arg11 第11引数
	 * @param arg12 第12引数
	 * @param arg13 第13引数
	 * @param arg14 第14引数
	 * @param arg15 第15引数
	 * @param arg16 第16引数
	 * @param arg17 第17引数
	 * @param arg18 第18引数
	 * @param arg19 第19引数
	 * @param arg20 第20引数
	 * @param <T1> 第1引数の型
	 * @param <T2> 第2引数の型
	 * @param <T3> 第3引数の型
	 * @param <T4> 第4引数の型
	 * @param <T5> 第5引数の型
	 * @param <T6> 第6引数の型
	 * @param <T7> 第7引数の型
	 * @param <T8> 第8引数の型
	 * @param <T9> 第9引数の型
	 * @param <T10> 第10引数の型
	 * @param <T11> 第11引数の型
	 * @param <T12> 第12引数の型
	 * @param <T13> 第13引数の型
	 * @param <T14> 第14引数の型
	 * @param <T15> 第15引数の型
	 * @param <T16> 第16引数の型
	 * @param <T17> 第17引数の型
	 * @param <T18> 第18引数の型
	 * @param <T19> 第19引数の型
	 * @param <T20> 第20引数の型
	 * @param <R> 戻り値の型
	 * @param <E> スローされる例外の型
	 * @return ラムダ式の戻り値
	 * @throws E ラムダ式がスローした例外
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R, E extends Throwable> R da(ThrowableFunction20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12, T13 arg13, T14 arg14, T15 arg15, T16 arg16, T17 arg17, T18 arg18, T19 arg19, T20 arg20) throws E {
		return of(action).da(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
	}
}
