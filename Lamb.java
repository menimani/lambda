package lambda;

/**
 * {@code Lamb} は、例外をスローするラムダ式を即時実行または保持して実行するためのユーティリティクラスです。
 * <p>
 * 各種 {@code FunctionalInterface} をラップし、引数の有無・戻り値の有無・例外の有無にかかわらず、
 * 統一的な形でラムダを扱うことができます。
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
 *   <li>{@code Consumer} … 引数あり、戻り値なし（最大5引数まで）</li>
 *   <li>{@code Function} … 引数あり、戻り値あり（最大5引数まで）</li>
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
 * @author [Your Name]
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
        void da() throws E;
    }

    /**
     * ラムダ式を保持します。（引数なし、戻り値なし）
     * <p>
     * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
     *
     * @param action 保持するラムダ式
     * @param <E> スローされる例外の型
     * @return 保持されたラムダオブジェクト
     */
    public static <E extends Throwable> ThrowableRunnable<E> of(ThrowableRunnable<E> action) {
        return action;
    }

    /**
     * ラムダ式を即時実行します。（引数なし、戻り値なし）
     *
     * @param action 実行するラムダ式
     * @param <E> スローされる例外の型
     * @throws E ラムダがスローした例外
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
     * @return 保持されたラムダオブジェクト
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
     * @return ラムダの戻り値
     * @throws E ラムダがスローした例外
     */
    public static <R, E extends Throwable> R da(ThrowableSupplier<R, E> action) throws E {
        return of(action).da();
    }

    // =========== ThrowableConsumer ===========

    /**
     * ラムダ式（引数１個、戻り値なし）
     * @param <T> 第1引数の型
     * @param <E> スローされる例外の型
     */
    @FunctionalInterface
    public interface ThrowableConsumer<T, E extends Throwable> {
        void da(T arg) throws E;
    }

    /**
     * ラムダ式を保持します。（引数１個、戻り値なし）
     * <p>
     * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
     *
     * @param action 保持するラムダ式
     * @param <T> 第1引数の型
     * @param <E> スローされる例外の型
     * @return 保持されたラムダオブジェクト
     */
    public static <T, E extends Throwable> ThrowableConsumer<T, E> of(ThrowableConsumer<T, E> action) {
        return action;
    }

    /**
     * ラムダ式を即時実行します。（引数１個、戻り値なし）
     *
     * @param action 実行するラムダ式
     * @param arg 第1引数
     * @param <T> 第1引数の型
     * @param <E> スローされる例外の型
     * @throws E ラムダがスローした例外
     */
    public static <T, E extends Throwable> void da(ThrowableConsumer<T, E> action, T arg) throws E {
        of(action).da(arg);
    }

    // =========== ThrowableConsumer2 ===========

    /**
     * ラムダ式（引数２個、戻り値なし）
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <E> スローされる例外の型
     */
    @FunctionalInterface
    public interface ThrowableConsumer2<T1, T2, E extends Throwable> {
        void da(T1 arg1, T2 arg2) throws E;
    }

    /**
     * ラムダ式を保持します。（引数２個、戻り値なし）
     * <p>
     * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
     *
     * @param action 保持するラムダ式
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <E> スローされる例外の型
     * @return 保持されたラムダオブジェクト
     */
    public static <T1, T2, E extends Throwable> ThrowableConsumer2<T1, T2, E> of(ThrowableConsumer2<T1, T2, E> action) {
        return action;
    }

    /**
     * ラムダ式を即時実行します。（引数２個、戻り値なし）
     *
     * @param action 実行するラムダ式
     * @param arg1 第1引数
     * @param arg2 第2引数
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <E> スローされる例外の型
     * @throws E ラムダがスローした例外
     */
    public static <T1, T2, E extends Throwable> void da(ThrowableConsumer2<T1, T2, E> action, T1 arg1, T2 arg2) throws E {
        of(action).da(arg1, arg2);
    }

    // =========== ThrowableConsumer3 ===========

    /**
     * ラムダ式（引数３個、戻り値なし）
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <T3> 第3引数の型
     * @param <E> スローされる例外の型
     */
    @FunctionalInterface
    public interface ThrowableConsumer3<T1, T2, T3, E extends Throwable> {
        void da(T1 arg1, T2 arg2, T3 arg3) throws E;
    }

    /**
     * ラムダ式を保持します。（引数３個、戻り値なし）
     * <p>
     * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
     *
     * @param action 保持するラムダ式
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <T3> 第3引数の型
     * @param <E> スローされる例外の型
     * @return 保持されたラムダオブジェクト
     */
    public static <T1, T2, T3, E extends Throwable> ThrowableConsumer3<T1, T2, T3, E> of(ThrowableConsumer3<T1, T2, T3, E> action) {
        return action;
    }

    /**
     * ラムダ式を即時実行します。（引数３個、戻り値なし）
     *
     * @param action 実行するラムダ式
     * @param arg1 第1引数
     * @param arg2 第2引数
     * @param arg3 第3引数
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <T3> 第3引数の型
     * @param <E> スローされる例外の型
     * @throws E ラムダがスローした例外
     */
    public static <T1, T2, T3, E extends Throwable> void da(ThrowableConsumer3<T1, T2, T3, E> action, T1 arg1, T2 arg2, T3 arg3) throws E {
        of(action).da(arg1, arg2, arg3);
    }

    // =========== ThrowableConsumer4 ===========

    /**
     * ラムダ式（引数４個、戻り値なし）
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <T3> 第3引数の型
     * @param <T4> 第4引数の型
     * @param <E> スローされる例外の型
     */
    @FunctionalInterface
    public interface ThrowableConsumer4<T1, T2, T3, T4, E extends Throwable> {
        void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4) throws E;
    }

    /**
     * ラムダ式を保持します。（引数４個、戻り値なし）
     * <p>
     * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
     *
     * @param action 保持するラムダ式
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <T3> 第3引数の型
     * @param <T4> 第4引数の型
     * @param <E> スローされる例外の型
     * @return 保持されたラムダオブジェクト
     */
    public static <T1, T2, T3, T4, E extends Throwable> ThrowableConsumer4<T1, T2, T3, T4, E> of(ThrowableConsumer4<T1, T2, T3, T4, E> action) {
        return action;
    }

    /**
     * ラムダ式を即時実行します。（引数４個、戻り値なし）
     *
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
     * @throws E ラムダがスローした例外
     */
    public static <T1, T2, T3, T4, E extends Throwable> void da(ThrowableConsumer4<T1, T2, T3, T4, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4) throws E {
        of(action).da(arg1, arg2, arg3, arg4);
    }

    // =========== ThrowableConsumer5 ===========

    /**
     * ラムダ式（引数５個、戻り値なし）
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <T3> 第3引数の型
     * @param <T4> 第4引数の型
     * @param <T5> 第5引数の型
     * @param <E> スローされる例外の型
     */
    @FunctionalInterface
    public interface ThrowableConsumer5<T1, T2, T3, T4, T5, E extends Throwable> {
        void da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5) throws E;
    }

    /**
     * ラムダ式を保持します。（引数５個、戻り値なし）
     * <p>
     * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
     *
     * @param action 保持するラムダ式
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <T3> 第3引数の型
     * @param <T4> 第4引数の型
     * @param <T5> 第5引数の型
     * @param <E> スローされる例外の型
     * @return 保持されたラムダオブジェクト
     */
    public static <T1, T2, T3, T4, T5, E extends Throwable> ThrowableConsumer5<T1, T2, T3, T4, T5, E> of(ThrowableConsumer5<T1, T2, T3, T4, T5, E> action) {
        return action;
    }

    /**
     * ラムダ式を即時実行します。（引数５個、戻り値なし）
     *
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
     * @throws E ラムダがスローした例外
     */
    public static <T1, T2, T3, T4, T5, E extends Throwable> void da(ThrowableConsumer5<T1, T2, T3, T4, T5, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5) throws E {
        of(action).da(arg1, arg2, arg3, arg4, arg5);
    }

    // =========== ThrowableFunction ===========

    /**
     * ラムダ式（引数１個、戻り値あり）
     * @param <T> 第1引数の型
     * @param <R> 戻り値の型
     * @param <E> スローされる例外の型
     */
    @FunctionalInterface
    public interface ThrowableFunction<T, R, E extends Throwable> {
        R da(T arg) throws E;
    }

    /**
     * ラムダ式を保持します。（引数１個、戻り値あり）
     * <p>
     * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
     *
     * @param action 保持するラムダ式
     * @param <T> 第1引数の型
     * @param <R> 戻り値の型
     * @param <E> スローされる例外の型
     * @return 保持されたラムダオブジェクト
     */
    public static <T, R, E extends Throwable> ThrowableFunction<T, R, E> of(ThrowableFunction<T, R, E> action) {
        return action;
    }

    /**
     * ラムダ式を即時実行します。（引数１個、戻り値あり）
     *
     * @param action 実行するラムダ式
     * @param arg 第1引数
     * @param <T> 第1引数の型
     * @param <R> 戻り値の型
     * @param <E> スローされる例外の型
     * @return ラムダの戻り値
     * @throws E ラムダがスローした例外
     */
    public static <T, R, E extends Throwable> R da(ThrowableFunction<T, R, E> action, T arg) throws E {
        return of(action).da(arg);
    }

    // =========== ThrowableFunction2 ===========

    /**
     * ラムダ式（引数２個、戻り値あり）
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <R> 戻り値の型
     * @param <E> スローされる例外の型
     */
    @FunctionalInterface
    public interface ThrowableFunction2<T1, T2, R, E extends Throwable> {
        R da(T1 arg1, T2 arg2) throws E;
    }

    /**
     * ラムダ式を保持します。（引数２個、戻り値あり）
     * <p>
     * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
     *
     * @param action 保持するラムダ式
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <R> 戻り値の型
     * @param <E> スローされる例外の型
     * @return 保持されたラムダオブジェクト
     */
    public static <T1, T2, R, E extends Throwable> ThrowableFunction2<T1, T2, R, E> of(ThrowableFunction2<T1, T2, R, E> action) {
        return action;
    }

    /**
     * ラムダ式を即時実行します。（引数２個、戻り値あり）
     *
     * @param action 実行するラムダ式
     * @param arg1 第1引数
     * @param arg2 第2引数
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <R> 戻り値の型
     * @param <E> スローされる例外の型
     * @return ラムダの戻り値
     * @throws E ラムダがスローした例外
     */
    public static <T1, T2, R, E extends Throwable> R da(ThrowableFunction2<T1, T2, R, E> action, T1 arg1, T2 arg2) throws E {
        return of(action).da(arg1, arg2);
    }

    // =========== ThrowableFunction3 ===========

    /**
     * ラムダ式（引数３個、戻り値あり）
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <T3> 第3引数の型
     * @param <R> 戻り値の型
     * @param <E> スローされる例外の型
     */
    @FunctionalInterface
    public interface ThrowableFunction3<T1, T2, T3, R, E extends Throwable> {
        R da(T1 arg1, T2 arg2, T3 arg3) throws E;
    }

    /**
     * ラムダ式を保持します。（引数３個、戻り値あり）
     * <p>
     * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
     *
     * @param action 保持するラムダ式
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <T3> 第3引数の型
     * @param <R> 戻り値の型
     * @param <E> スローされる例外の型
     * @return 保持されたラムダオブジェクト
     */
    public static <T1, T2, T3, R, E extends Throwable> ThrowableFunction3<T1, T2, T3, R, E> of(ThrowableFunction3<T1, T2, T3, R, E> action) {
        return action;
    }

    /**
     * ラムダ式を即時実行します。（引数３個、戻り値あり）
     *
     * @param action 実行するラムダ式
     * @param arg1 第1引数
     * @param arg2 第2引数
     * @param arg3 第3引数
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <T3> 第3引数の型
     * @param <R> 戻り値の型
     * @param <E> スローされる例外の型
     * @return ラムダの戻り値
     * @throws E ラムダがスローした例外
     */
    public static <T1, T2, T3, R, E extends Throwable> R da(ThrowableFunction3<T1, T2, T3, R, E> action, T1 arg1, T2 arg2, T3 arg3) throws E {
        return of(action).da(arg1, arg2, arg3);
    }

    // =========== ThrowableFunction4 ===========

    /**
     * ラムダ式（引数４個、戻り値あり）
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <T3> 第3引数の型
     * @param <T4> 第4引数の型
     * @param <R> 戻り値の型
     * @param <E> スローされる例外の型
     */
    @FunctionalInterface
    public interface ThrowableFunction4<T1, T2, T3, T4, R, E extends Throwable> {
        R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4) throws E;
    }

    /**
     * ラムダ式を保持します。（引数４個、戻り値あり）
     * <p>
     * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
     *
     * @param action 保持するラムダ式
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <T3> 第3引数の型
     * @param <T4> 第4引数の型
     * @param <R> 戻り値の型
     * @param <E> スローされる例外の型
     * @return 保持されたラムダオブジェクト
     */
    public static <T1, T2, T3, T4, R, E extends Throwable> ThrowableFunction4<T1, T2, T3, T4, R, E> of(ThrowableFunction4<T1, T2, T3, T4, R, E> action) {
        return action;
    }

    /**
     * ラムダ式を即時実行します。（引数４個、戻り値あり）
     *
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
     * @return ラムダの戻り値
     * @throws E ラムダがスローした例外
     */
    public static <T1, T2, T3, T4, R, E extends Throwable> R da(ThrowableFunction4<T1, T2, T3, T4, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4) throws E {
        return of(action).da(arg1, arg2, arg3, arg4);
    }

    // =========== ThrowableFunction5 ===========

    /**
     * ラムダ式（引数５個、戻り値あり）
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <T3> 第3引数の型
     * @param <T4> 第4引数の型
     * @param <T5> 第5引数の型
     * @param <R> 戻り値の型
     * @param <E> スローされる例外の型
     */
    @FunctionalInterface
    public interface ThrowableFunction5<T1, T2, T3, T4, T5, R, E extends Throwable> {
        R da(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5) throws E;
    }

    /**
     * ラムダ式を保持します。（引数５個、戻り値あり）
     * <p>
     * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。
     *
     * @param action 保持するラムダ式
     * @param <T1> 第1引数の型
     * @param <T2> 第2引数の型
     * @param <T3> 第3引数の型
     * @param <T4> 第4引数の型
     * @param <T5> 第5引数の型
     * @param <R> 戻り値の型
     * @param <E> スローされる例外の型
     * @return 保持されたラムダオブジェクト
     */
    public static <T1, T2, T3, T4, T5, R, E extends Throwable> ThrowableFunction5<T1, T2, T3, T4, T5, R, E> of(ThrowableFunction5<T1, T2, T3, T4, T5, R, E> action) {
        return action;
    }

    /**
     * ラムダ式を即時実行します。（引数５個、戻り値あり）
     *
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
     * @return ラムダの戻り値
     * @throws E ラムダがスローした例外
     */
    public static <T1, T2, T3, T4, T5, R, E extends Throwable> R da(ThrowableFunction5<T1, T2, T3, T4, T5, R, E> action, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5) throws E {
        return of(action).da(arg1, arg2, arg3, arg4, arg5);
    }
}