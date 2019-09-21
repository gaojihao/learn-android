import React, { Component } from 'react';
import { View,FlatList,StyleSheet,Text } from 'react-native';
import EmptyView from './../../component/EmptyView';
import config from './../../tools/config';


export default class TimeLineScreen extends Component {

  static navigationOptions = {
    title: '信息',
  };

  constructor(props) {
    super(props);
    this.state = JSON.parse('{\"list\":[{\"operation\":\"问卷随访\",\"createTime\":\"2019-09-02 20:38:35\",\"title\":\"推送随访表单\",\"realName\":\"管理员\"},{\"operation\":\"问卷随访\",\"createTime\":\"2019-09-02 19:07:05\",\"title\":\"推送随访表单\",\"realName\":\"管理员\"},{\"operation\":\"在线咨询\",\"createTime\":\"2019-08-30 10:57:49\",\"title\":\"发送咨询\",\"realName\":\"赵东海\"},{\"operation\":\"在线咨询\",\"createTime\":\"2019-08-30 10:38:38\",\"title\":\"回复咨询\",\"realName\":\"管理员\"},{\"operation\":\"在线咨询\",\"createTime\":\"2019-08-29 23:07:15\",\"title\":\"发送咨询\",\"realName\":\"赵东海\"},{\"operation\":\"在线咨询\",\"createTime\":\"2019-08-29 23:07:11\",\"title\":\"发送咨询\",\"realName\":\"赵东海\"},{\"operation\":\"在线咨询\",\"createTime\":\"2019-08-29 22:54:35\",\"title\":\"发送咨询\",\"realName\":\"赵东海\"},{\"operation\":\"在线咨询\",\"createTime\":\"2019-08-22 22:50:59\",\"title\":\"发送咨询\",\"realName\":\"赵东海\"},{\"operation\":\"用户注册\",\"createTime\":\"2019-08-13 21:47:40\",\"title\":\"注册成功\",\"realName\":\"赵东海\"}]}');
  }

  render() {

    return (
      <React.Fragment>
        {
          this.state.list.length >= 1?
          <FlatList
          style={{
              backgroundColor: '#F5F5FC',
          }}
          data={this.state.list}
          showsVerticalScrollIndicator={false}
          showsHorizontalScrollIndicator={false}
          keyExtractor={(item, index) => `${item.createTime}${index}`}
          renderItem={this._renderItem}
      />
          :
          <EmptyView emptyTitle={'暂无内容'}/>
        }
      </React.Fragment>
    );
  }

  _renderItem = ({item,index}) =>(
    <View style={styles.itemStyle} key = {`${item.createTime}${index}`}>
      <View style={styles.sideStyle}>
        <View style={styles.cicleStyle}/>
        <View style={styles.lineStyle}/>
      </View>
      <View style={styles.sideStyle}>
        <View style={styles.topStyle}>
          <View style={styles.titleStyle}><Text>{item.title}</Text></View>
          <Text style={{fontSize:13,color:'#999'}}>{item.createTime}</Text>
        </View>
        <Text style={{fontSize:13,color:'#999999',alignSelf:'flex-start'}}>{item.operation}</Text>
      </View>
    </View>
    )
}

const styles = StyleSheet.create({
  itemStyle:{
      width:config.screenWidth,
      height:80,
      backgroundColor:'white',
      flexDirection:'row',
  },

  sideStyle:{
    marginLeft:15,
    flexDirection:'column',
    alignItems:'center'
  },


  cicleStyle:{
    width:10,
    height:10,
    backgroundColor:'#FE5E86',
    borderRadius:5,
  },

  lineStyle:{
    width:config.onePix,
    flex:1,
    backgroundColor:'#eeeeee',
  },

  topStyle:{
    flexDirection:'row',
    marginBottom:14,
  },

  titleStyle:{
    marginRight:6,
    color:'#333',
    fontSize:15,
  },

});


