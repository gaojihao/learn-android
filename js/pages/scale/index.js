import React, { Component } from 'react';
import { View,FlatList,StyleSheet,Text,TouchableOpacity } from 'react-native';
import EmptyView from './../../component/EmptyView';
import config from './../../tools/config';

export default class ScaleScreen extends Component {

  static navigationOptions = {
    title: '评测量表',
  };

  constructor(props) {
    super(props);
    this.state = JSON.parse('{\"list\":[{\"surveyId\":\"11ee9sfsdafsd57857dg454d58g586c2\",\"realName\":\"管理员\",\"surveyName\":\"ASQ年龄与发育进程问卷-第三版（6个月）\",\"title\":\"ASQ年龄与发育进程问卷-第三版（6个月）\",\"createAt\":\"2019-06-21 19:59:10\",\"subTitle\":\"问卷中问到的问题，您的宝宝有些可能已经能完成或已经表现出来，有些可能还不能完成或您还没有注意到。对每个问题，请您在“是”即经常能，“有时是”即刚刚会还不熟练或有时会有时不会，“否”即还不会，三个选项中勾选一个。\",\"url\":\"/survey/selftest/11ee9sfsdafsd57857dg454d58g586c2\"}]}');
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
          keyExtractor={(item, index) => `${item.id}${index}`}
          renderItem={this._renderItem}
      />
          :
          <EmptyView emptyTitle={'暂无内容'}/>
        }
      </React.Fragment>
    );
  }

  _renderItem = ({item,index}) =>(
    <TouchableOpacity activeOpacity={0.8} style={styles.itemStyle} key = {`${item.id}${index}`}>
      <Text style={styles.nornalText}>{item.title}</Text>
      <Text numberOfLines={2} style={styles.snamllText}>{item.subTitle}</Text>
      <Text style={styles.snamllText}>{item.createAt}</Text>
    </TouchableOpacity>
    )
}

const styles = StyleSheet.create({
  itemStyle:{
      width:config.screenWidth,
      height:110,
      backgroundColor:'white',
      flexDirection:'column',
      borderBottomColor:'#eeeeee',
      justifyContent:'space-around',
      paddingHorizontal:15,
      borderBottomWidth:config.onePix,
  },

  topStyle:{
  },

  bottomStyle:{
    flexDirection:'row',
    alignItems:'center',
    justifyContent:'space-between',
  },

  nornalText:{
    fontSize:15,
    color:'#333',
  },

  snamllText:{
    fontSize:13,
    color:'#999',
  }

});


