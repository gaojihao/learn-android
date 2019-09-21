import React from 'react';
import {
  Image,
  TouchableOpacity,
  ScrollView,
  Text,
  StatusBar,
  StyleSheet,
  View,
} from 'react-native';
import config from './../../tools/config';

let headerList = [{
  url:'zixun',
  title:'咨询',
  router:'zixun'
},{
  url:'yuyue',
  title:'预约',
  router:'yuyue'
},{
  url:'comment',
  title:'互动',
  router:''
},{
  url:'info',
  title:'信息',
  router:'timeLine'
},{
  url:'mine',
  title:'我的',
  router:'mine'
}]

let serviceList = [{
  url:'wenzhen',
  title:'在线问诊',
  router:'wenzhen'
},{
  url:'jiaoyu',
  title:'健康教育',
  router:'education'
},{
  url:'yongyao',
  title:'康复指南',
  router:'recovery'
},{
  url:'suifang',
  title:'家庭随访',
  router:'follow'
}]

let selfService = [{
  url:'zice',
  router:'scale'
},{
  url:'chaxun',
  router:'report'
},{
  url:'wenjuan',
  router:'question'
}]

export default class HomeScreen extends React.Component {
  static navigationOptions = {
    title: '私人医助',
    headerRight:null,
  };

  //banner
  _renderBanner(){
    return (<Image source={{uri: 'banner'}} style={{width: config.screenWidth, height: 383*config.screenWidth/900}}/>);
  }

  //header
  _renderHeader(){
    return (
      <View style={styles.headerStyle}>
        {
          headerList.map((item,index) => {
            return(
            <TouchableOpacity activeOpacity={1} style={styles.headerItem} onPress={() => this.props.navigation.navigate(item.router)}>
              <Image source={{uri: item.url}} style={{width: 60, height:60}}/>
              <Text style={styles.miniText}>{item.title}</Text>
            </TouchableOpacity>
        )
          })
        }
      </View>
    );
  }

  //服务项目
  _renderService(){
    return (
      <React.Fragment>
        {this._renderSectionHeader('服务项目')}
        <View style={styles.headerStyle}>
        {
          serviceList.map((item,index) => {
            return(
            <TouchableOpacity activeOpacity={1} style={styles.headerItem} onPress={() => this.props.navigation.navigate(item.router)}>
              <Image source={{uri: item.url}} style={{width: 60, height:60,marginBottom:8}}/>
              <Text style={styles.miniText}>{item.title}</Text>
            </TouchableOpacity>
            )
          })
        }
      </View>
      </React.Fragment>
    );
  }

  //自助服务
  _renderSelf(){
    return (
      <React.Fragment>
        {this._renderSectionHeader('自助服务')}
        <View style={styles.bottomStyle}>
        {
          selfService.map((item,index) => {
            return(
            <TouchableOpacity activeOpacity={1} style={styles.selfServiceSection} onPress={() => this.props.navigation.navigate(item.router)}>
              <Image source={{uri: item.url}} style={{width: config.screenWidth-30, height:(config.screenWidth-30)*383/900,}}/>
            </TouchableOpacity>
            )
          })
        }
      </View>
      </React.Fragment>
    );
  }

  _renderSectionHeader(title){
    return (
    <View style={styles.sectionHeader}>
      <View style={{backgroundColor:'#1567b9',width:6,height:18,borderRadius:3,marginLeft:15,marginRight:8}}/>
      <Text style={{fontSize:15,color:'#1567b9'}}>{title}</Text>
    </View>)
  }

  render() {
    return (
      <ScrollView style={{backgroundColor:'#EFEFF9',flex: 1}}>
        <StatusBar backgroundColor="#FC427B" barStyle="light-content" />
        {
          this._renderBanner()
        }
        {
          this._renderHeader()
        }
        {
          this._renderService()
        }
        {
          this._renderSelf()
        }
      </ScrollView>
    );
  }

}

const styles = StyleSheet.create({

  headerStyle:{
    width:config.screenWidth,
    justifyContent:'space-around',
    flexDirection: 'row',
    alignItems:'center',
    height:120,
    backgroundColor:'white',
  },

  headerItem:{
    display:'flex',
    flex: 1,
    alignItems:'center', 
    flexDirection: 'column'
  },

  miniText:{
    fontSize:15,
    color:'#666',
  },

  normalText:{
    fontSize:16,
    color:'#666',
  },

  sectionHeader:{
    marginTop:10,
    height:40,
    flexDirection:'row',
    alignItems:'center',
    backgroundColor:'white',
    borderBottomColor:'#edefef',
    borderBottomWidth:config.onePix,
  },


  bottomStyle:{
    width:config.screenWidth,
    backgroundColor:'white',
    paddingBottom:20,
  },

  selfServiceSection:{
    width:config.screenWidth,
    alignItems:'center',
    justifyContent:'center',
    marginTop:10,
  }

  });