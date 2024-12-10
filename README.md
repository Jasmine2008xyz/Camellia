# Camellia

## 介绍

Camellia 是一个开源的 Xposed 模块，旨在对QQ进行功能扩展、为 QQ 用户带来更好的使用体验

## 使用方法

- 获取最新版本： [![GitHub release](https://img.shields.io/github/release/Jasmine2008xyz/Camellia.svg)](https://github.com/Jasmine2008xyz/Camellia/releases/latest)
- 请确保您会使用 **Xposed 框架**来装载本模块
- 如果您使用的是 **[Lsposed](https://github.com/LSPosed/LSPosed)** ，请勾选模块作用域为 **QQ**
- **[注意] 本模块的支持版本范围为9.0~9.1.23+(最新版)**
- 
### 2024/12/10 +++++公告+++++
Camellia已无开源意义，本仓库已停止更新，谢谢支持。

## 额外说明

模块尚在开发中，因此出现一系列**未知 bug 及不可控情况**是正常的情况， 我们也更推荐您使用最新版本来避免此类情况的发生
也欢迎您在 issue  提出功能开发建议以及 bug 反馈。

本模块仅供学习参考，请勿将本模块用于非法用途。

给我们点点Star，不要逼我求你。

## 编译
- JDK>=17.0.9
- NDK ... 你看我们用了吗？(

------------

### 已经支持的功能
- 修改图片外显
- 修改图片大小
- 长按消息菜单显示消息MsgRecord
- 文件重命名apk.1
- 屏蔽拍一拍Timing
- 一键20赞
- 解锁卡片消息左滑
- 阻止Java层闪退
- 长按文本修改内容
- 语音面板
- 屏蔽卡屏文本
- 跳转网页功能
- 解锁输入框限制
- 屏蔽QQ通知
- ......

### 即将支持的功能
- ~~随机开发者女装照~~
- 长按发送按钮发送卡片消息
- 百变气泡


### 我们的优势

#### 真·清晰易懂的代码逻辑
没有使用复杂的依赖库，使得我们的代码逻辑清晰易懂易移植。

因为太复杂的库我们两个看不懂也不会用。

#### 看似规范且条理清晰的注解配上逆天的代码

以下逆天代码与注解曾真实存在于项目之中：
```java
      // 开始添加Item
  root.addView(
          Getbutton(
                  "Java插件",
                  v -> {
                      /** 这里因为直接升起不了Activity，所以为了偷懒，先退出当前活动 **/
                      /**
                       * 不要紧的，我会在JavaPluginActivity里重写退出按钮，他们就只有返回键可以按，退出后自动升回来，反正速度很快他们看不出来
                       */
                       onBackPressed();                         
                       startActivity(
                              new Intent(SettingActivity.this, JavaPluginActivity.class));
                  }));
``` 
不过其实大部分地方是根本没有注解的(

#### 简短的变量名压缩源码大小
以下代码摘自公式的防撤回项目
```java
public static void Seach(ArrayList a) {
    try {

      Object q = a.get(0);
            
      String b = FieldUtils.GetField(q, "msgId").toString();
      String g = FieldUtils.GetField(q, "senderUin").toString();
      String ll = FieldUtils.GetField(q, "sendNickName").toString();
      MsgRecordTable.senduin.put(b, g);
      MsgRecordTable.sendnick.put(b, ll);
      //MsgRecordTable.msg.put(b,q);
      ArrayList p = FieldUtils.GetField(q, "elements");
      Object m = p.get(0);
      Integer elementtype = FieldUtils.GetField(m, "elementType");
      if (elementtype == 1) {
        Object textele = FieldUtils.GetField(m, "textElement");

        String mm = FieldUtils.GetField(textele, "content");
        MsgRecordTable.msglist.put(b, mm);
      }

    } catch (Exception e) {
      ForLogUtils.Debug("Search", e);
    }
  }
```



### 交流讨论
- Telegram(https://t.me/wsy666HD)
- QQ通知群 837012640
- QQ交流群 902327702


### 支持我们
![](http://103.24.204.23/lzj/赞助码.jpg)

