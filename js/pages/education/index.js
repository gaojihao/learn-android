import React, { Component } from 'react';
import { View,FlatList,StyleSheet,Text,TouchableOpacity } from 'react-native';
import EmptyView from './../../component/EmptyView';
import config from './../../tools/config';

export default class EducationScreen extends Component {
  static navigationOptions = {
    title: '健康教育',
  };

  constructor(props) {
    super(props);
    this.state = JSON.parse('{\"list\":[{\"creater\":\"管理员\",\"id\":\"57f397c30e2e46228d944bb50d2257f413\",\"title\":\"老年人得了严重的褥疮\",\"url\":\"/share/healthtopic/57f397c30e2e46228d944bb50d2257f413\",\"date\":\"2019-09-09 08:43:19\"},{\"creater\":\"管理员\",\"id\":\"054f5eed60ca4bf28fefd9eb2715482612\",\"title\":\"老年褥疮\",\"url\":\"/share/healthtopic/054f5eed60ca4bf28fefd9eb2715482612\",\"date\":\"2019-09-09 08:37:53\"},{\"creater\":\"管理员\",\"id\":\"86b567a137c749b2ac727b36b37853009\",\"title\":\"什么是口腔感觉系统失调？\",\"url\":\"/share/healthtopic/86b567a137c749b2ac727b36b37853009\",\"date\":\"2019-06-16 18:50:47\"},{\"creater\":\"管理员\",\"id\":\"740e169324ce4c52bf01a286f7d9dd925\",\"title\":\"婴幼儿粗大运动和精细运动发育简介\",\"url\":\"/share/healthtopic/740e169324ce4c52bf01a286f7d9dd925\",\"date\":\"2019-06-16 18:47:05\"},{\"creater\":\"管理员\",\"id\":\"b6511b0f221841b2a6e26e39c34e7b244\",\"title\":\"婴幼儿进食技能发展程序\",\"url\":\"/share/healthtopic/b6511b0f221841b2a6e26e39c34e7b244\",\"date\":\"2019-06-16 18:46:16\"}]}');
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


