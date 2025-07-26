# Lamb

`Lamb` は、例外をスローするラムダ式を即時実行または保持して後から実行するためのユーティリティクラスです。  
引数の有無・戻り値の有無にかかわらず、統一的なインターフェースで扱えるのが特徴です。

## 特徴
- **`da()` メソッド**：ラムダ式を即時実行  
- **`of()` メソッド**：ラムダ式を保持し、任意のタイミングで `da()` により実行  
- **最大 20 引数対応**：`Consumer` / `Function` 系は最大 20 引数までサポート  
- **依存関係なし**：単一ファイル `Lamb.java` をプロジェクトに追加するだけで利用可能

## 使用例

```java
// 引数なし・戻り値あり（即時実行）
final String value = Lamb.da(() -> "Hello");

// 引数あり・戻り値あり（即時実行）
final int doubled = Lamb.da((x) -> x * 2, 21);

// ラムダ式の保持と後からの実行
final var supplier = Lamb.of(() -> "cached");
final String result = supplier.da(); // 任意のタイミングで実行

// 引数ありの保持と後からの実行
final var func = Lamb.of((x, y) -> x + y);
final String joined = func.da("Hello, ", "World");
```

## ドキュメント
Javadoc は GitHub Pages で公開されています：  
[https://menimani.github.io/lambda/](https://menimani.github.io/lambda/)

## 配置方法
`src/lambda/Lamb.java` を好きなパッケージにコピーして利用可能です。  
ビルドや依存関係は一切不要です。
