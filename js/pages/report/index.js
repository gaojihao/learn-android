import React, { Component } from 'react';
import { View,FlatList,StyleSheet,Image,Text,TouchableOpacity } from 'react-native';
import EmptyView from './../../component/EmptyView';
import config from './../../tools/config';

export default class ReportScreen extends Component {
  static navigationOptions = {
    title: '报告查询',
  };

  constructor(props) {
    super(props);
    this.state = JSON.parse('{\"list\":[{\"departmentName\":\"康复科\",\"doctorName\":\"王五\",\"id\":\"e9d34e7999344d9486375adb2fed6a395\",\"title\":\"康复建议标题\",\"date\":\"2019-09-04\",\"headimg\":\"/upload/avatar/x.jpg\",\"hospitalName\":\"新华医院\",\"url\":\"/share/diseasecareplan/e9d34e7999344d9486375adb2fed6a395\"}]}');
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
      <View style={styles.contentStyle}>
        <Text style={styles.darkText}>{item.date}</Text>
        <Text style={styles.nornalText}>{item.title}</Text>
        <Text style={styles.snamllText}>{item.hospitalName} {item.departmentName}</Text>
      </View>
      <View style={styles.doctorStyle}>
        <Image source={{uri: 'https://www.gomeiguo.com'+item.headimg}} style={{width: 60, height:60,marginLeft:15}}/>
        <Text style={styles.snamllText}>{item.doctorName}</Text>
      </View>
    </TouchableOpacity>
    )
}

const styles = StyleSheet.create({
  itemStyle:{
      width:config.screenWidth,
      height:100,
      backgroundColor:'white',
      flexDirection:'row',
      borderBottomColor:'#eeeeee',
      alignItems:'center',
      borderBottomWidth:config.onePix,
  },

  contentStyle:{
    marginLeft:15,
    flex:1,
    flexDirection:'column',
    justifyContent:'space-between',
  },

  doctorStyle:{
    flexDirection:'column',
    alignItems:'center',
    justifyContent:'center',
    width:100,
    height:100,
  },

  darkText:{
    fontSize:18,
    color:'#333',
  },

  nornalText:{
    fontSize:14,
    color:'#666',
    marginTop:6,
    marginBottom:10,
  },

  snamllText:{
    marginTop:4,
    fontSize:12,
    color:'#999',
  }

});


