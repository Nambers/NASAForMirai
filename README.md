# NASAForMirai
WIP
来源: [NASA公开API](https://api.nasa.gov/index.html#browseAPI)

## 配置项
配置格式是json
- apikey: 在Nasa申请的apikey, string类型, 在[这里](https://api.nasa.gov/index.html)申请, 只需要(虚假的)名字和邮箱
- commandStatus: 可选, map<String, boolean>类型, key(第一项) 为API名字的一个(在下面:APOD/EARTH/EPIC/Mars...) 代表这个api是否启用, 比如 `{"EPIC":false}`, `#EPIC 2020-1-1` 就不会有任何作用
- commandTets: 可选, map<String, String>类型, key 为api名字的一个, value(第二项)代表触发的字符串, 默认为 `#`加各个API名字, 所以默认就是 `#EPIC 2020-1-1` 就可以触发, 如果在这里设置`{"EPIC": "abc"}`, `abc 2020-1-1`就会触发

## 目前已经实现的api
### APOD
当天或者指定时间的天文照片

返回转发信息, 里面有照片等其他信息

参数:
- date: 拍摄日期, 以 yyyy-MM-dd格式

### EARTH
地球一部分表面照片(和地图的卫星图片差不多), 调用的是google的api所以可能无法使用

返回单张照片

参数: 
- lon: 经度
- lat: 纬度
- date: 拍摄日期, yyyy-MM-dd

### EPIC
地球全部的照片

返回转发信息里面包含图片和其他信息

目前只支持 [natural/date](https://api.nasa.gov/index.html#:~:text=natural%20color%20imagery.-,natural/date,-YYYY%2DMM%2DDD)

参数:
- date: 拍摄时间, yyyy-MM-dd

### Mars
火星车拍的照片, 有些早的可能发不出

返回转发信息里面包含图片和其他信息

参数：
- earth_date: 拍摄时间, yyyy-MM-dd
- \<camera\>: 相机名字 FHAZ, RHAZ, MAST, CHEMCAM, MAHLI, MARDI, NAVCAM, PANCAM, MINITES, [对应含义](https://api.nasa.gov/index.html#:~:text=named%20as%20follows%3A-,Rover%20Cameras,-Abbreviation)

## license
```
Copyright (C) 2021-2021 Eritque arcus and contributors.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or any later version(in your opinion).

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```
