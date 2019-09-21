import React, { Component } from 'react';
import { View,FlatList,StyleSheet,Text,TouchableOpacity } from 'react-native';
import EmptyView from './../../component/EmptyView';
import config from './../../tools/config';

export default class RecoveryScreen extends Component {
  static navigationOptions = {
    title: '康复指南',
  };

  constructor(props) {
    super(props);
    this.state = JSON.parse('{\"list\":[{\"creater\":\"管理员\",\"id\":\"aade5bc202004ba3b22a9c3bf7e0748710\",\"title\":\"关节活动度训练\",\"url\":\"/share/healthtopic/aade5bc202004ba3b22a9c3bf7e0748710\",\"date\":\"2019-06-16 18:51:36\"},{\"creater\":\"管理员\",\"id\":\"de6d0fae2a984ed3af0fc24426ca0c568\",\"title\":\"发育性髋关节脱位的家庭康复\",\"url\":\"/share/healthtopic/de6d0fae2a984ed3af0fc24426ca0c568\",\"date\":\"2019-06-16 18:49:36\"},{\"creater\":\"管理员\",\"id\":\"f2198e78c07f4b639487c1708f122f8e7\",\"title\":\"重视脑卒中康复训练\",\"url\":\"/share/healthtopic/f2198e78c07f4b639487c1708f122f8e7\",\"date\":\"2019-06-16 18:48:29\"}]}');
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
      <View style={styles.topStyle}>
        <Text style={styles.nornalText}>{item.title}</Text>
      </View>
      <View style={styles.bottomStyle}>
        <Text style={styles.snamllText}>{item.date}</Text>
        <Text style={styles.snamllText}>{item.creater}</Text>
      </View>
    </TouchableOpacity>
    )
}

const styles = StyleSheet.create({
  itemStyle:{
      width:config.screenWidth,
      height:80,
      backgroundColor:'white',
      flexDirection:'column',
      borderBottomColor:'#eeeeee',
      justifyContent:'space-between',
      padding:15,
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
    fontSize:12,
    color:'#999',
  }

});


