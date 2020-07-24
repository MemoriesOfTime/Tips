# **Tips**
一款多功能显示插件

- ####使用说明
   - ####用户:
     1. 将本插件安装到` plugins`文件夹
     2. 安装前置插件 参考 **`Tips变量.txt`** 文件
     3. 修改` config.yml` 配置  
     
     > (小贴士) config内的 default 为全地图通用显示  
      若想实现每个 地图 不同的内容 只需要复制粘贴
      delfault 内容即可 然后将 default 替换成 
      那个地图的名称
     
    - ####开发者 
      > 注册变量
      >> 创建一个类继承 `BaseVariable `  
      调用`BaseVariable `类里的` addStrReplaceString`方法  
      传入 key(变量名称) value(显示内容)  
      然后在 插件的 onEnable 里增加 
       ```java
      Api.registerVariables("插件名",<? extends BaseVariable>class);
       ```
      >修改内容
      >> 调用Api类内的方法 修改
   