param (
    [int]$count = 100,
    [string]$package = "lambda"
)

$lines = @()
$lines += "package ${package};"
$lines += ""
$lines += "/**"
$lines += " * {@code Lamb} �́A��O���X���[���郉���_���𑦎����s�܂��͕ێ����Ď��s���邽�߂̃��[�e�B���e�B�N���X�ł��B"
$lines += " * <p>"
$lines += " * �e�� {@code FunctionalInterface} �����b�v���A�����̗L���E�߂�l�̗L���E��O�̗L���ɂ�����炸�A"
$lines += " * ����I�Ȍ`�Ń����_�����������Ƃ��ł��܂��B"
$lines += " * </p>"
$lines += " *"
$lines += " * <h2>��ȗp�r</h2>"
$lines += " * <ul>"
$lines += " *   <li>{@code da()} ���\�b�h�F�����_���̑������s</li>"
$lines += " *   <li>{@code of()} ���\�b�h�F�����_���̕ێ��E�ォ����s</li>"
$lines += " * </ul>"
$lines += " *"
$lines += " * <h2>�g�p��</h2>"
$lines += " * <pre>{@code"
$lines += " * // �����Ȃ��E�߂�l����i�������s�j"
$lines += " * final String value = Lamb.da(() -> `"Hello`");"
$lines += " *"
$lines += " * // ��������E�߂�l����i�������s�j"
$lines += " * final int doubled = Lamb.da((x) -> x * 2, 21);"
$lines += " *"
$lines += " * // �����_���̕ێ��ƌォ��̎��s"
$lines += " * final var supplier = Lamb.of(() -> `"cached`");"
$lines += " * final String result = supplier.da(); // �C�ӂ̃^�C�~���O�Ŏ��s"
$lines += " *"
$lines += " * // ��������̕ێ��ƌォ��̎��s"
$lines += " * final var func = Lamb.of((x, y) -> x + y);"
$lines += " * final String joined = func.da(`"Hello, `", `"World`");"
$lines += " * }</pre>"
$lines += " *"
$lines += " * <h2>�����K��</h2>"
$lines += " * <ul>"
$lines += " *   <li>{@code Runnable} �c �����Ȃ��A�߂�l�Ȃ�</li>"
$lines += " *   <li>{@code Supplier} �c �����Ȃ��A�߂�l����</li>"
$lines += " *   <li>{@code Consumer} �c ��������A�߂�l�Ȃ��i�ő�${count}�����܂Łj</li>"
$lines += " *   <li>{@code Function} �c ��������A�߂�l����i�ő�${count}�����܂Łj</li>"
$lines += " * </ul>"
$lines += " *"
$lines += " * <h2>��O�`��</h2>"
$lines += " * <p>"
$lines += " * �S�Ă� {@code da()} ���\�b�h�́A�Ώۂ̃����_������O���X���[�����ꍇ�A"
$lines += " * ���̗�O�����b�v�����ɂ��̂܂܌Ăяo�����ɓ`�d�����܂��B"
$lines += " * </p>"
$lines += " *"
$lines += " * <p>"
$lines += " * Java�W���� {@code Runnable} �� {@code Function} �̓`�F�b�N��O�ɑΉ��ł��܂��񂪁A"
$lines += " * �{�N���X�� {@code throws} �𖾎�����݌v�ɂ��A���S�ɗ�O�Ή����\�ł��B"
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
$lines += "`t * �����_���i�����Ȃ��A�߂�l�Ȃ��j"
$lines += "`t * @param <E> �X���[������O�̌^"
$lines += "`t */"
$lines += "`t@FunctionalInterface"
$lines += "`tpublic interface ThrowableRunnable<E extends Throwable> {"
$lines += "`t`t/**"
$lines += "`t`t * �����_���𑦎����s���܂��B�i�����Ȃ��A�߂�l�Ȃ��j"
$lines += "`t`t * @throws E �����_�����X���[������O"
$lines += "`t`t */"
$lines += "`t`tvoid da() throws E;"
$lines += "`t}"
$lines += "`t"
$lines += "`t/**"
$lines += "`t * �����_����ێ����܂��B�i�����Ȃ��A�߂�l�Ȃ��j"
$lines += "`t * <p>"
$lines += "`t * �������s�ł͂Ȃ��A�ォ�� {@code da(...)} ���Ăяo�����ƂŔC�ӂ̃^�C�~���O�Ŏ��s�\�ł��B"
$lines += "`t *"
$lines += "`t * @param action �ێ����郉���_��"
$lines += "`t * @param <E> �X���[������O�̌^"
$lines += "`t * @return �ێ����ꂽ�����_��"
$lines += "`t */"
$lines += "`tpublic static <E extends Throwable> ThrowableRunnable<E> of(ThrowableRunnable<E> action) {"
$lines += "`t`treturn action;"
$lines += "`t}"
$lines += "`t"
$lines += "`t/**"
$lines += "`t * �����_���𑦎����s���܂��B�i�����Ȃ��A�߂�l�Ȃ��j"
$lines += "`t *"
$lines += "`t * @param action ���s���郉���_��"
$lines += "`t * @param <E> �X���[������O�̌^"
$lines += "`t * @throws E �����_�����X���[������O"
$lines += "`t */"
$lines += "`tpublic static <E extends Throwable> void da(ThrowableRunnable<E> action) throws E {"
$lines += "`t`tof(action).da();"
$lines += "`t}"
$lines += "`t"
$lines += "`t// =========== ThrowableSupplier ==========="
$lines += "`t"
$lines += "`t/**"
$lines += "`t * �����_���i�����Ȃ��A�߂�l����j"
$lines += "`t * @param <R> �߂�l�̌^"
$lines += "`t * @param <E> �X���[������O�̌^"
$lines += "`t */"
$lines += "`t@FunctionalInterface"
$lines += "`tpublic interface ThrowableSupplier<R, E extends Throwable> {"
$lines += "`t`t/**"
$lines += "`t`t * �����_���𑦎����s���܂��B�i�����Ȃ��A�߂�l����j"
$lines += "`t`t * @return �����_���̖߂�l"
$lines += "`t`t * @throws E �����_�����X���[������O"
$lines += "`t`t */"
$lines += "`t`tR da() throws E;"
$lines += "`t}"
$lines += "`t"
$lines += "`t/**"
$lines += "`t * �����_����ێ����܂��B�i�����Ȃ��A�߂�l����j"
$lines += "`t * <p>"
$lines += "`t * �������s�ł͂Ȃ��A�ォ�� {@code da(...)} ���Ăяo�����ƂŔC�ӂ̃^�C�~���O�Ŏ��s�\�ł��B"
$lines += "`t *"
$lines += "`t * @param action �ێ����郉���_��"
$lines += "`t * @param <R> �߂�l�̌^"
$lines += "`t * @param <E> �X���[������O�̌^"
$lines += "`t * @return �ێ����ꂽ�����_��"
$lines += "`t */"
$lines += "`tpublic static <R, E extends Throwable> ThrowableSupplier<R, E> of(ThrowableSupplier<R, E> action) {"
$lines += "`t`treturn action;"
$lines += "`t}"
$lines += "`t"
$lines += "`t/**"
$lines += "`t * �����_���𑦎����s���܂��B�i�����Ȃ��A�߂�l����j"
$lines += "`t *"
$lines += "`t * @param action ���s���郉���_��"
$lines += "`t * @param <R> �߂�l�̌^"
$lines += "`t * @param <E> �X���[������O�̌^"
$lines += "`t * @return �����_���̖߂�l"
$lines += "`t * @throws E �����_�����X���[������O"
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
    $lines += "`t * �����_���i����${i}�A�߂�l�Ȃ��j"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param <T$j> ��${j}�����̌^"
    }
    $lines += "`t * @param <E> �X���[������O�̌^"
    $lines += "`t */"
    $lines += "`t@FunctionalInterface"
    $lines += "`tpublic interface $interfaceName<" + ($typeParams -join ", ") + "> {"
    $lines += "`t`t/**"
    $lines += "`t`t * �����_���𑦎����s���܂��B�i����${i}�A�߂�l�Ȃ��j"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t`t * @param arg$j ��${j}����"
    }
    $lines += "`t`t * @throws E �����_�����X���[������O"
    $lines += "`t`t */"
    $lines += "`t`tvoid da(" + ($params -join ", ") + ") throws E;"
    $lines += "`t}"
    $lines += "`t"

    # of���\�b�h
    $lines += "`t/**"
    $lines += "`t * �����_����ێ����܂��B�i����${i}�A�߂�l�Ȃ��j"
    $lines += "`t * <p>"
    $lines += "`t * �������s�ł͂Ȃ��A�ォ�� {@code da(...)} ���Ăяo�����ƂŔC�ӂ̃^�C�~���O�Ŏ��s�\�ł��B"
    $lines += "`t * @param action �ێ����郉���_��"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param <T$j> ��${j}�����̌^"
    }
    $lines += "`t * @param <E> �X���[������O�̌^"
    $lines += "`t * @return �ێ����ꂽ�����_��"
    $lines += "`t */"
    $lines += "`tpublic static <" + ($typeParams -join ", ") + "> $interfaceName<" + ($argTypeParams -join ", ") + "> of($interfaceName<" + ($argTypeParams -join ", ") + "> action) {"
    $lines += "`t`treturn action;"
    $lines += "`t}"
    $lines += "`t"

    # da���\�b�h
    $lines += "`t/**"
    $lines += "`t * �����_���𑦎����s���܂��B�i����${i}�A�߂�l�Ȃ��j"
    $lines += "`t * @param action ���s���郉���_��"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param arg$j ��${j}����"
    }
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param <T$j> ��${j}�����̌^"
    }
    $lines += "`t * @param <E> �X���[������O�̌^"
    $lines += "`t * @throws E �����_�����X���[������O"
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
    $lines += "`t * �����_���i����${i}�A�߂�l����j"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param <T$j> ��${j}�����̌^"
    }
    $lines += "`t * @param <R> �߂�l�̌^"
    $lines += "`t * @param <E> �X���[������O�̌^"
    $lines += "`t */"
    $lines += "`t@FunctionalInterface"
    $lines += "`tpublic interface $interfaceName<" + ($typeParams -join ", ") + "> {"
    $lines += "`t`t/**"
    $lines += "`t`t * �����_���𑦎����s���܂��B�i����${i}�A�߂�l����j"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t`t * @param arg$j ��${j}����"
    }
    $lines += "`t`t * @return �����_���̖߂�l"
    $lines += "`t`t * @throws E �����_�����X���[������O"
    $lines += "`t`t */"
    $lines += "`t`tR da(" + ($params -join ", ") + ") throws E;"
    $lines += "`t}"
    $lines += "`t"

    # of���\�b�h
    $lines += "`t/**"
    $lines += "`t * �����_����ێ����܂��B�i����${i}�A�߂�l����j"
    $lines += "`t * <p>"
    $lines += "`t * �������s�ł͂Ȃ��A�ォ�� {@code da(...)} ���Ăяo�����ƂŔC�ӂ̃^�C�~���O�Ŏ��s�\�ł��B"
    $lines += "`t * @param action �ێ����郉���_��"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param <T$j> ��${j}�����̌^"
    }
    $lines += "`t * @param <R> �߂�l�̌^"
    $lines += "`t * @param <E> �X���[������O�̌^"
    $lines += "`t * @return �ێ����ꂽ�����_��"
    $lines += "`t */"
    $lines += "`tpublic static <" + ($typeParams -join ", ") + "> $interfaceName<" + ($argTypeParams -join ", ") + "> of($interfaceName<" + ($argTypeParams -join ", ") + "> action) {"
    $lines += "`t`treturn action;"
    $lines += "`t}"
    $lines += "`t"

    # da���\�b�h
    $lines += "`t/**"
    $lines += "`t * �����_���𑦎����s���܂��B�i����${i}�A�߂�l����j"
    $lines += "`t * @param action ���s���郉���_��"
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param arg$j ��${j}����"
    }
    for ($j = 1; $j -le $i; $j++) {
        $lines += "`t * @param <T$j> ��${j}�����̌^"
    }
    $lines += "`t * @param <R> �߂�l�̌^"
    $lines += "`t * @param <E> �X���[������O�̌^"
    $lines += "`t * @return �����_���̖߂�l"
    $lines += "`t * @throws E �����_�����X���[������O"
    $lines += "`t */"
    $lines += "`tpublic static <" + ($typeParams -join ", ") + "> R da($interfaceName<" + ($argTypeParams -join ", ") + "> action, " + ($params -join ", ") + ") throws E {"
    $lines += "`t`treturn of(action).da(" + ($paramDefs[1..$paramDefs.Count] -join ", ") + ");"
    $lines += "`t}"
}

$lines += "}"
$outputPath = "Lamb.java"
$lines -join "`n" | Set-Content -Encoding UTF8 $outputPath
Write-Host "Generated $outputPath"