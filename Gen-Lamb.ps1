param (
    [int]$count = 100,
    [string]$package = "lambda"
)

$lines = @()
$lines += "package ${package};"
$lines += ""
$lines += "/**"
$lines += " * {@code Lamb} は、例外をスローするラムダ式を即時実行または保持して実行するためのユーティリティクラスです。"
$lines += " * <p>"
$lines += " * 各種 {@code FunctionalInterface} をラップし、引数の有無・戻り値の有無・例外の有無にかかわらず、"
$lines += " * 統一的な形でラムダ式を扱うことができます。"
$lines += " * </p>"
$lines += " *"
$lines += " * <h2>主な用途</h2>"
$lines += " * <ul>"
$lines += " *   <li>{@code da()} メソッド：ラムダ式の即時実行</li>"
$lines += " *   <li>{@code of()} メソッド：ラムダ式の保持・後から実行</li>"
$lines += " * </ul>"
$lines += " *"
$lines += " * <h2>使用例</h2>"
$lines += " * <pre>{@code"
$lines += " * // 引数なし・戻り値あり（即時実行）"
$lines += " * final String value = Lamb.da(() -> `"Hello`");"
$lines += " *"
$lines += " * // 引数あり・戻り値あり（即時実行）"
$lines += " * final int doubled = Lamb.da((x) -> x * 2, 21);"
$lines += " *"
$lines += " * // ラムダ式の保持と後からの実行"
$lines += " * final var supplier = Lamb.of(() -> `"cached`");"
$lines += " * final String result = supplier.da(); // 任意のタイミングで実行"
$lines += " *"
$lines += " * // 引数ありの保持と後からの実行"
$lines += " * final var func = Lamb.of((x, y) -> x + y);"
$lines += " * final String joined = func.da(`"Hello, `", `"World`");"
$lines += " * }</pre>"
$lines += " *"
$lines += " * <h2>命名規則</h2>"
$lines += " * <ul>"
$lines += " *   <li>{@code Runnable} … 引数なし、戻り値なし</li>"
$lines += " *   <li>{@code Supplier} … 引数なし、戻り値あり</li>"
$lines += " *   <li>{@code Consumer} … 引数あり、戻り値なし（最大${count}引数まで）</li>"
$lines += " *   <li>{@code Function} … 引数あり、戻り値あり（最大${count}引数まで）</li>"
$lines += " * </ul>"
$lines += " *"
$lines += " * <h2>例外伝搬</h2>"
$lines += " * <p>"
$lines += " * 全ての {@code da()} メソッドは、対象のラムダ式が例外をスローした場合、"
$lines += " * その例外をラップせずにそのまま呼び出し元に伝播させます。"
$lines += " * </p>"
$lines += " *"
$lines += " * <p>"
$lines += " * Java標準の {@code Runnable} や {@code Function} はチェック例外に対応できませんが、"
$lines += " * 本クラスは {@code throws} を明示する設計により、安全に例外対応が可能です。"
$lines += " * </p>"
$lines += " *"
$lines += " * @author menimani" 
$lines += " * @since 1.0"
$lines += " */"
$lines += "public final class Lamb {"
$lines += "`tprivate Lamb() {}"
$lines += "`t"
$lines += "`t// =========== ThrowableRunnable ==========="
$lines += "`t"
$lines += "`t/**"
$lines += "`t * ラムダ式（引数なし、戻り値なし）"
$lines += "`t * @param <E> スローされる例外の型"
$lines += "`t */"
$lines += "`t@FunctionalInterface"
$lines += "`tpublic interface ThrowableRunnable<E extends Throwable> {"
$lines += "`t`t/**"
$lines += "`t`t * ラムダ式を即時実行します。（引数なし、戻り値なし）"
$lines += "`t`t * @throws E ラムダ式がスローした例外"
$lines += "`t`t */"
$lines += "`t`tvoid da() throws E;"
$lines += "`t}"
$lines += "`t"
$lines += "`t/**"
$lines += "`t * ラムダ式を保持します。（引数なし、戻り値なし）"
$lines += "`t * <p>"
$lines += "`t * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。"
$lines += "`t *"
$lines += "`t * @param action 保持するラムダ式"
$lines += "`t * @param <E> スローされる例外の型"
$lines += "`t * @return 保持されたラムダ式"
$lines += "`t */"
$lines += "`tpublic static <E extends Throwable> ThrowableRunnable<E> of(ThrowableRunnable<E> action) {"
$lines += "`t`treturn action;"
$lines += "`t}"
$lines += "`t"
$lines += "`t/**"
$lines += "`t * ラムダ式を即時実行します。（引数なし、戻り値なし）"
$lines += "`t *"
$lines += "`t * @param action 実行するラムダ式"
$lines += "`t * @param <E> スローされる例外の型"
$lines += "`t * @throws E ラムダ式がスローした例外"
$lines += "`t */"
$lines += "`tpublic static <E extends Throwable> void da(ThrowableRunnable<E> action) throws E {"
$lines += "`t`tof(action).da();"
$lines += "`t}"
$lines += "`t"
$lines += "`t// =========== ThrowableSupplier ==========="
$lines += "`t"
$lines += "`t/**"
$lines += "`t * ラムダ式（引数なし、戻り値あり）"
$lines += "`t * @param <R> 戻り値の型"
$lines += "`t * @param <E> スローされる例外の型"
$lines += "`t */"
$lines += "`t@FunctionalInterface"
$lines += "`tpublic interface ThrowableSupplier<R, E extends Throwable> {"
$lines += "`t`t/**"
$lines += "`t`t * ラムダ式を即時実行します。（引数なし、戻り値あり）"
$lines += "`t`t * @return ラムダ式の戻り値"
$lines += "`t`t * @throws E ラムダ式がスローした例外"
$lines += "`t`t */"
$lines += "`t`tR da() throws E;"
$lines += "`t}"
$lines += "`t"
$lines += "`t/**"
$lines += "`t * ラムダ式を保持します。（引数なし、戻り値あり）"
$lines += "`t * <p>"
$lines += "`t * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。"
$lines += "`t *"
$lines += "`t * @param action 保持するラムダ式"
$lines += "`t * @param <R> 戻り値の型"
$lines += "`t * @param <E> スローされる例外の型"
$lines += "`t * @return 保持されたラムダ式"
$lines += "`t */"
$lines += "`tpublic static <R, E extends Throwable> ThrowableSupplier<R, E> of(ThrowableSupplier<R, E> action) {"
$lines += "`t`treturn action;"
$lines += "`t}"
$lines += "`t"
$lines += "`t/**"
$lines += "`t * ラムダ式を即時実行します。（引数なし、戻り値あり）"
$lines += "`t *"
$lines += "`t * @param action 実行するラムダ式"
$lines += "`t * @param <R> 戻り値の型"
$lines += "`t * @param <E> スローされる例外の型"
$lines += "`t * @return ラムダ式の戻り値"
$lines += "`t * @throws E ラムダ式がスローした例外"
$lines += "`t */"
$lines += "`tpublic static <R, E extends Throwable> R da(ThrowableSupplier<R, E> action) throws E {"
$lines += "`t`treturn of(action).da();"
$lines += "`t}"

# ThrowableConsumer
$lines += "`t"
$lines += "`t// =========== ThrowableConsumer ==========="
for ($i = 1; $i -le $count; $i++) {
    $lines += "`t"
    $interfaceName = "`ThrowableConsumer$i"
    $paramDefs = @("action")
    for ($j = 1; $j -le $i; $j++) {
        $paramDefs += "arg$j"
    }
    $typeParams = @()
    for ($j = 1; $j -le $i; $j++) {
        $typeParams += "`T$j"
    }
    $typeParams += "E extends Throwable"
    $argTypeParams = @()
    for ($j = 1; $j -le $i; $j++) {
        $argTypeParams += "`T$j"
    }
    $argTypeParams += "E"
    $params = @()
    for ($j = 1; $j -le $i; $j++) {
        $params += "`T$j arg$j"
    }

    # FunctionalInterface
    $lines += "`t/**"
    $lines += "`t * ラムダ式（引数${i}個、戻り値なし）"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param <T$j> 第${j}引数の型"
    }
    $lines += "`t * @param <E> スローされる例外の型"
    $lines += "`t */"
    $lines += "`t@FunctionalInterface"
    $lines += "`tpublic interface $interfaceName<" + ($typeParams -join ", ") + "> {"
    $lines += "`t`t/**"
    $lines += "`t`t * ラムダ式を即時実行します。（引数${i}個、戻り値なし）"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t`t * @param arg$j 第${j}引数"
    }
    $lines += "`t`t * @throws E ラムダ式がスローした例外"
    $lines += "`t`t */"
    $lines += "`t`tvoid da(" + ($params -join ", ") + ") throws E;"
    $lines += "`t}"
    $lines += "`t"

    # ofメソッド
    $lines += "`t/**"
    $lines += "`t * ラムダ式を保持します。（引数${i}個、戻り値なし）"
    $lines += "`t * <p>"
    $lines += "`t * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。"
    $lines += "`t * @param action 保持するラムダ式"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param <T$j> 第${j}引数の型"
    }
    $lines += "`t * @param <E> スローされる例外の型"
    $lines += "`t * @return 保持されたラムダ式"
    $lines += "`t */"
    $lines += "`tpublic static <" + ($typeParams -join ", ") + "> $interfaceName<" + ($argTypeParams -join ", ") + "> of($interfaceName<" + ($argTypeParams -join ", ") + "> action) {"
    $lines += "`t`treturn action;"
    $lines += "`t}"
    $lines += "`t"

    # daメソッド
    $lines += "`t/**"
    $lines += "`t * ラムダ式を即時実行します。（引数${i}個、戻り値なし）"
    $lines += "`t * @param action 実行するラムダ式"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param arg$j 第${j}引数"
    }
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param <T$j> 第${j}引数の型"
    }
    $lines += "`t * @param <E> スローされる例外の型"
    $lines += "`t * @throws E ラムダ式がスローした例外"
    $lines += "`t */"
    $lines += "`tpublic static <" + ($typeParams -join ", ") + "> void da($interfaceName<" + ($argTypeParams -join ", ") + "> action, " + ($params -join ", ") + ") throws E {"
    $lines += "`t`tof(action).da(" + ($paramDefs[1..$paramDefs.Count] -join ", ") + ");"
    $lines += "`t}"
}


# ThrowableFunction
$lines += "`t"
$lines += "`t// =========== ThrowableFunction ==========="
for ($i = 1; $i -le $count; $i++) {
    $lines += "`t"
    $interfaceName = "`ThrowableFunction$i"
    $paramDefs = @("action")
    for ($j = 1; $j -le $i; $j++) {
        $paramDefs += "arg$j"
    }
    $typeParams = @()
    for ($j = 1; $j -le $i; $j++) {
        $typeParams += "`T$j"
    }
    $typeParams += "R"
    $typeParams += "E extends Throwable"
    $argTypeParams = @()
    for ($j = 1; $j -le $i; $j++) {
        $argTypeParams += "`T$j"
    }
    $argTypeParams += "R"
    $argTypeParams += "E"
    $params = @()
    for ($j = 1; $j -le $i; $j++) {
        $params += "`T$j arg$j"
    }

    # FunctionalInterface
    $lines += "`t/**"
    $lines += "`t * ラムダ式（引数${i}個、戻り値あり）"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param <T$j> 第${j}引数の型"
    }
    $lines += "`t * @param <R> 戻り値の型"
    $lines += "`t * @param <E> スローされる例外の型"
    $lines += "`t */"
    $lines += "`t@FunctionalInterface"
    $lines += "`tpublic interface $interfaceName<" + ($typeParams -join ", ") + "> {"
    $lines += "`t`t/**"
    $lines += "`t`t * ラムダ式を即時実行します。（引数${i}個、戻り値あり）"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t`t * @param arg$j 第${j}引数"
    }
    $lines += "`t`t * @return ラムダ式の戻り値"
    $lines += "`t`t * @throws E ラムダ式がスローした例外"
    $lines += "`t`t */"
    $lines += "`t`tR da(" + ($params -join ", ") + ") throws E;"
    $lines += "`t}"
    $lines += "`t"

    # ofメソッド
    $lines += "`t/**"
    $lines += "`t * ラムダ式を保持します。（引数${i}個、戻り値あり）"
    $lines += "`t * <p>"
    $lines += "`t * 即時実行ではなく、後から {@code da(...)} を呼び出すことで任意のタイミングで実行可能です。"
    $lines += "`t * @param action 保持するラムダ式"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param <T$j> 第${j}引数の型"
    }
    $lines += "`t * @param <R> 戻り値の型"
    $lines += "`t * @param <E> スローされる例外の型"
    $lines += "`t * @return 保持されたラムダ式"
    $lines += "`t */"
    $lines += "`tpublic static <" + ($typeParams -join ", ") + "> $interfaceName<" + ($argTypeParams -join ", ") + "> of($interfaceName<" + ($argTypeParams -join ", ") + "> action) {"
    $lines += "`t`treturn action;"
    $lines += "`t}"
    $lines += "`t"

    # daメソッド
    $lines += "`t/**"
    $lines += "`t * ラムダ式を即時実行します。（引数${i}個、戻り値あり）"
    $lines += "`t * @param action 実行するラムダ式"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param arg$j 第${j}引数"
    }
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param <T$j> 第${j}引数の型"
    }
    $lines += "`t * @param <R> 戻り値の型"
    $lines += "`t * @param <E> スローされる例外の型"
    $lines += "`t * @return ラムダ式の戻り値"
    $lines += "`t * @throws E ラムダ式がスローした例外"
    $lines += "`t */"
    $lines += "`tpublic static <" + ($typeParams -join ", ") + "> R da($interfaceName<" + ($argTypeParams -join ", ") + "> action, " + ($params -join ", ") + ") throws E {"
    $lines += "`t`treturn of(action).da(" + ($paramDefs[1..$paramDefs.Count] -join ", ") + ");"
    $lines += "`t}"
}

$lines += "}"
$outputPath = "Lamb.java"
$lines -join "`n" | Set-Content -Encoding UTF8 $outputPath
Write-Host "Generated $outputPath"