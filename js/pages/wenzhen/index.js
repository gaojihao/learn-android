import React, { Component } from 'react';
import { View,FlatList,StyleSheet,Image,Text,TouchableOpacity } from 'react-native';
import EmptyView from './../../component/EmptyView';
import config from './../../tools/config';

export default class WenZhenScreen extends Component {

  static navigationOptions = {
    title: '在线问诊',
  };

  constructor(props) {
    super(props);
    this.state = JSON.parse('{\"list\":[{\"departmentName\":\"康复科\",\"doctorName\":\"王五\",\"id\":\"19c12035467c4091b13a11cb381d3b0a1\",\"position\":\"杜青\",\"title\":\"新生儿先天性斜颈\",\"date\":\"2019-06-17 21:12:52\",\"headimg\":\"/upload/avatar/x.jpg\",\"doctorId\":7,\"hospitalName\":\"新华医院\",\"url\":\"/share/healthtopic/19c12035467c4091b13a11cb381d3b0a1\"},{\"departmentName\":null,\"doctorName\":\"管理员\",\"id\":\"9971adfecf434371a0db06610f73697f2\",\"position\":null,\"title\":\"康复医学工程\",\"date\":\"2019-06-18 10:06:59\",\"headimg\":\"/upload/avatar/0/1.jpg\",\"doctorId\":1,\"hospitalName\":null,\"url\":\"/share/healthtopic/9971adfecf434371a0db06610f73697f2\"}]}');
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
      <Image source={{uri: 'https://www.gomeiguo.com'+item.headimg}} style={{width: 60, height:60,marginLeft:15}}/>
      <View style={styles.contentStyle}>
        <Text style={styles.nornalText}>{item.hospitalName}</Text>
        <Text style={styles.snamllText}>{item.departmentName} {item.title}</Text>
        <Text style={styles.snamllText}>{item.doctorName} {item.position}</Text>
      </View>
    </TouchableOpacity>
    )
}

const styles = StyleSheet.create({
  itemStyle:{
      width:config.screenWidth,
      height:80,
      backgroundColor:'white',
      flexDirection:'row',
      borderBottomColor:'#eeeeee',
      alignItems:'center',
      borderBottomWidth:config.onePix,
  },

  contentStyle:{
    marginLeft:4,
    flexDirection:'column',
    justifyContent:'space-evenly',
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


