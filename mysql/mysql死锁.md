# <a name="top">死锁</a>

+ <a href="#innodb-lock">Innodb的锁类型</a>









----

## <a name="innodb-lock">Innodb的锁类型</a>













----

## <a name="reference">参考</a>

+ <a href="https://mp.weixin.qq.com/s?__biz=MzI4NjExMDA4NQ==&mid=2648453552&idx=1&sn=b6e68345edb3b8e4b0bb366405cde071&chksm=f3c9695ac4bee04cf1ce6f7d70a7b9d6acba64e6dc209d783d493a78c9cdd7432b9748750e7d&mpshare=1&scene=1&srcid=1106ksm2FyOcBBqrKumeUIYR&sharer_sharetime=1604642485044&sharer_shareid=97fc2856e7b026a4fb9b5b573ad33e20&key=0fc8929eb7125045e7c7eb3b771044cbbdfe98ccdd6382e609b44caee5dd62e248b75bfb1abcd4f0f77cb158f519237df792cb2efbc62655812be7c11012537e877b7043d20c34a1ec266b45ce512c83e96a1b10b38b404cd2906fb60d7f3f84b8e2367a6d6a1c2ba2d71e28022836bf6bc2700e8c8a4b822a843afe61e363d6&ascene=1&uin=MjYwMDQwMjYzNA%3D%3D&devicetype=Windows+10+x64&version=63000039&lang=zh_CN&exportkey=AwwFKZ9GW8CgS1rLlJkyWpQ%3D&pass_ticket=eh81slXMgWL3llGlG9n7U8oQC8B8bYCUcAPjAbCsX%2BN4kT9XVXW8ciNpR%2Fa2bnht&wx_header=0">MySQL中RR模式下死锁一例</a>
+ <a href="https://mp.weixin.qq.com/s?__biz=MjM5NzAzMTY4NQ==&mid=2653934297&idx=1&sn=1b4415f14d1350a00cf4866817d2395e&chksm=bd3b48b38a4cc1a57f5e3bdbacb09c3ced1bf70484aca7de764c651e2ce40688ad7aa90704b3&mpshare=1&scene=1&srcid=0821Tdh3dDyWneyonxBs34F2&sharer_sharetime=1598838116220&sharer_shareid=97fc2856e7b026a4fb9b5b573ad33e20&key=acb454f1294cbca39dfcea6ecdfce466f49d71668e2a56aa10e6ccc014d6992478ea44745de0740311f4f9ebf2e7b0defcc122cfd39e700e9e7545cefca3acf9fa4a720031503ee1d707da9a3270c8f59634a3338a0172a20afde36fc504ec5b65dee72ed928349a2c0d601c15fcbca39db445f2a170e095ae6e1cee0f055150&ascene=1&uin=MjYwMDQwMjYzNA%3D%3D&devicetype=Windows+10+x64&version=63000039&lang=zh_CN&exportkey=A1kF3q3xRKn%2BpAjBE7XgXrA%3D&pass_ticket=eh81slXMgWL3llGlG9n7U8oQC8B8bYCUcAPjAbCsX%2BN4kT9XVXW8ciNpR%2Fa2bnht&wx_header=0">死锁案例一</a>
+ <a href="https://mp.weixin.qq.com/s?__biz=MjM5NzAzMTY4NQ==&mid=2653934344&idx=1&sn=9b1a4ec019b19dd8a8367e34e3b11ee6&chksm=bd3b49628a4cc0742336c9684c704012198da94e28aff48be179b553e2557cd2a125fd13232d&mpshare=1&scene=1&srcid=0831t7IVihW50Z9FjXGwoS2y&sharer_sharetime=1598857930620&sharer_shareid=97fc2856e7b026a4fb9b5b573ad33e20&key=3f5a89b6b3fbe8f13f787f1a38d77fb469923620385efea0ab2fe3fa96168acdab0718a4cff779cf88b0c86f82b92f87c371db397fa31ec76240c2fd6557995f654cf8a984a5a595e85bbad7151857de809bb7f04563460552b6e60403474546d397fab4c9eab7e5c8bb2dce67eab7030c099b8d2a3e315f55273a293a313ac5&ascene=1&uin=MjYwMDQwMjYzNA%3D%3D&devicetype=Windows+10+x64&version=63000039&lang=zh_CN&exportkey=A86I6Cwu3etoTdGHAUlsoh4%3D&pass_ticket=eh81slXMgWL3llGlG9n7U8oQC8B8bYCUcAPjAbCsX%2BN4kT9XVXW8ciNpR%2Fa2bnht&wx_header=0">死锁案例二</a>
+ <a href="https://mp.weixin.qq.com/s?__biz=MjM5NzAzMTY4NQ==&mid=2653934380&idx=1&sn=3266e158add079c382329644e65d5318&chksm=bd3b49468a4cc050c699592a4970a33c61db386589f6199b8d9d6a0b1ac4eb3aabc5d52aa3dc&mpshare=1&scene=1&srcid=08318DanH7DyPZoZUSc7aIzb&sharer_sharetime=1598804242284&sharer_shareid=97fc2856e7b026a4fb9b5b573ad33e20&key=99eb9f72cf4534e24f1b7bf320b59d26da47a4e9d1c2b8a6af1bc9c2427dd9ea96491b0a1a40336d6367647da74b866eb2a4bf2be3eaeb04fea06ee5c735de768093379606d768a21675a29f82737d2a9d04e467dafaf846ef4f5880d100e12c0e761aae8b48f701b62a97a2527eb7853644a14b1cb82d43c0eb49ec9639e250&ascene=1&uin=MjYwMDQwMjYzNA%3D%3D&devicetype=Windows+10+x64&version=63000039&lang=zh_CN&exportkey=A1YtCnc9uy0kgmLElZeckUY%3D&pass_ticket=eh81slXMgWL3llGlG9n7U8oQC8B8bYCUcAPjAbCsX%2BN4kT9XVXW8ciNpR%2Fa2bnht&wx_header=0">死锁案例三</a>