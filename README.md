# bCore Driver for Android

Play Storeにて公開中のAndroidアプリケーション「bCore Driver for Android」のソースファイルです。

## bCoreとは

Vagabond Worksさんが開発した汎用多目的超小型リモコンロボットコアユニットです。
詳しくは、[Vagabond Works [バガボンド　ワークス] 　bCoreとは？](http://vagabondworks.jp/blog-category-26.html) を参照してください。

## bCore Driver for Androidについて

Vagabond Worksさんが開発したiOS向けアプリ、[bDriver](https://itunes.apple.com/us/app/bdriver/id1017376059?mt=8) を参考に
Android向けに開発したアプリケーションです。
Play Storeでの掲載情報は[こちら](https://play.google.com/store/apps/details?id=net.shaga_workshop.bcoredriverforandroid)をご覧ください。

### システム要件

* Androi4.3以上がインストールされた、BluetoothLEに対応したAndroid端末であれば使用可能です。
* Xperia Z(Android4.4)、および、Nexus7(2013/Android6.0)で動作確認しています。

### 開発環境

* Android Stuido

### 依存ライブラリ

* [CatHandsGendroid](https://github.com/cattaka/CatHandsGendroid) を使って、デバイス情報を保存しています。

	利用方法は、[AndroidのSQLiteの面倒臭いを簡単にする - Qiita](http://qiita.com/cattaka/items/1edd041c59cbcfeb6ff4) を参考にしました。

* [bcore_lib](https://github.com/shaga/bcore_lib) を使って、bCoreとの通信をしています。

## ライセンスについて

このアプリケーションはApache License, Version 2.0 ( http://www.apache.org/licenses/LICENSE-2.0 )のライセンスで配布されている成果物を含んでいます。
