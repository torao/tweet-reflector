個人的に Twitter Sample Stream を使用して Twitter のつぶやきを収集する目的で使っているプログラム。

## Usage
`sbt run` で `yyyy-mm-dd.csv` 形式のファイルへツイートを出力して行く。

* `--japanese-only` 日本語(ひらがな、カタカナ)の混じっているツイートのみを保存
* `--output {console|file|riak}`
* `--format {json|csv}`

## LICENSE
Apache License 2.0

