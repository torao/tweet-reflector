Twitter Sample Stream を使用して Twitter のつぶやきを収集する目的で使っているソース。

## Usage
`sbt run` で標準出力へツイートを出力します。`--output file` を指定した場合は `yyyy-mm-dd.csv` 形式のファイルに保存されます。

* `--japanese-only` 日本語(ひらがな、カタカナ)の混じっているツイートのみを保存します。
* `--output {console|file|riak}` 出力先の指定します。
* `--format {json|csv}` 出力フォーマットを指定します。

## LICENSE
Apache License 2.0

