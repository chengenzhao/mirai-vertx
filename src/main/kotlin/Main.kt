import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain
import net.mamoe.mirai.utils.BotConfiguration
import javax.swing.GroupLayout

suspend fun main(){
  val QQ = 123L
  val password = "g"
  val groupId = 9769L
  initQQ(QQ,password,groupId){
    println(senderName)
   message.forEach {
     when (it) {
       is Image -> {
         println(it.imageId)
         //图片url
         println("http://gchat.qpic.cn/gchatpic_new/${bot.id}/0-0-${
           it.imageId.substring(1..36)
             .replace("-", "")
         }/0?term=2")
       }
       is PlainText -> {
         println(it.content)
       }
       else -> {}
     }
   }
  }

}

/**
 * @param qq QQ号
 * @param password 密码
 * @param groupId 群号
 * @param onMsg 接到信息后的回调 看main函数我提供了一个默认打印到控制台的实现
 */
suspend fun initQQ(qq:Long,password:String,groupId:Long,onMsg : GroupMessageEvent.() -> Unit) {

  val bot = BotFactory.newBot(qq, password) {
    //参考文档 这个可以关掉log
    // noBotLog()
    heartbeatStrategy = BotConfiguration.HeartbeatStrategy.STAT_HB
  }
  //注意 这里需要处理一下当前终端第一次登录的验证  Windows上面会弹出一个swing组件 linux无GUI的则是命令行打印一串网址
  //  1,进入网址 会看到一个滑块 此时打开F12 再进行滑块操作 完成后查看网络报文 寻找一个response里面 有名称为cap_union_new_verify的请求 查看响应里面的ticket字段填进去就行了
  //  2,完成滑块之后会在GUI/命令行里面再有一个网址，进去后选择扫码，用你的手机QQ扫码一下就行了
  bot.login()
  bot.eventChannel.subscribeGroupMessages {
    sentFrom(groupId).invoke {
      onMsg(this)
    }
  }
}