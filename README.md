# CtScore
Minecraft Score Plugin

## 插件特性

可自定义积分<br>
提供api可配合各类插件使用<br>
支持papi变量<br>
支持积分排行<br>
自带商城系统<br>
基于 Kotlin / Taboolib 开发<br>

## 使用介绍

本插件本意是需要配合其他插件来完成一些积分功能的插件在配置文件中可配置多种积分类型
这些积分可以配置一些购买的物品

### PAPI变量

%ctscore_number_积分id%  查询积分数量<br>

%ctscore_rank_积分id_数字%  查询积分排行中某个位置的玩家名<br>
%ctscore_rankplayer_积分id_me%  查询本人积分排行<br>
%ctscore_rankplayer_积分id_玩家名%  查询某个人的积分排行<br>

### 指令和权限

所有指令都需要一个前置权限 ctscore.access<br>
/cts look 积分id 玩家名[可选] 查询玩家积分 -  权限 ctscore.look ctscore.look.other<br>
/cts give 玩家名 积分id 数量 增加玩家积分 - 权限 ctscore.give<br>
/cts take 玩家名 积分id 数量 减少玩家积分 - 权限 ctscore.take<br>
/cts set 玩家名 积分id 数量 设置玩家积分 - 权限 ctscore.set<br>
/cts reset 玩家名 积分id 重置玩家积分 - 权限 ctscore.reset<br>
/cts pay 玩家名 积分名称 数量 转账 - 权限 ctscore.pay<br>
/cts buy 商品名 购买指定商品 - 权限 ctscore.buy<br>

op指令:<br>
/cts reload 重载配置文件 - 权限 ctscore.reload<br>
/cts load [yaml/mysql/sqlite] 从其他数据源导入数据 - 权限 ctscore.load<br>
导入之前切记要删除数据库内所有数据，不然会保留原有数据