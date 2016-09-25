#使用Data Binding实现列表的单条刷新
让我们想想传统的多线程下载动态刷新listView的做法：
```java
        private void updateSingleRow(ListView listView, long id) {  
      
            if (listView != null) {  
                int start = listView.getFirstVisiblePosition();  
                for (int i = start, j = listView.getLastVisiblePosition(); i <= j; i++)  
                    if (id == ((Messages) listView.getItemAtPosition(i)).getId()) {  
                        View view = listView.getChildAt(i - start);  
                        getView(i, view, listView);  
                        break;  
                    }  
            }  
        }  
```
`getFirstVisiblePosition()` 该方法获取当前状态下list的第一个可见item的position。
`getLastVisiblePosition()` 该方法获取当前状态下list的最后一个可见item的position。
`getItemAtPosition（int position）` 该方法返回当前状态下position位置上listView的convertView
我们必须使用id来判断所有可见条目，当id相同的时候才更新对应的UI,那么有没有不用遍历判断的方法呢，当然有的，那就是每次更新的时候都绑定每条item的viewHolder，例如:
```java
public View getView(View convertView,int position ... ){
    /*缓存convertView的代码*/
    if(viewHolder.lastBean != null)
        viewHolder.lastBean.holder = null;
    viewHolder.lastBean = bean;
    return convertView;
}
```
这样当我们的数据更新的时候，就可以直接通过Bean.holder来直接通过对应的UI更新，当然如果当前的Bean不在显示范围内的话，就是null.也就不需要更新了。但是以上两种方法的列表单条刷新都是需要自己通过UI线程来更新的，有没有更加简洁的方法吗，那接下来就是使用DataBinding的方式来实现列表单条刷新。
使用DataBinding我们首先要在工程的build.gradle中设置有效果：
```java
    dataBinding {
        enabled = true
    }
```
之后修改我们要添加Binding的layout,开头必须是<layout> ， 再通过 <data>引用我们的数据，可以参考Demo中的list_item.xml。
当我们构建好了xml文件之后必须reBuild一下工程，这样Android Studio就会直接生成layout对应的类，比如list_item.xml就会生成ListItemBinding类，这样就可以在HomeAdapter中创建绑定了：
```java
    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item,
                parent,
                false);
        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }
```
而我们要更新数据也很简单：
```java
    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ImageInfo info = mDatas.get(position);
        holder.getBinding().setInfo(info);
        holder.getBinding().setPresenter(mPresenter);
    }
```
我在list_item.xml中引用了两个类，一个是`ImageInfo`一个是`Presenter`,`Presenter`处理了程序的主要逻辑，其为下载任务设置了四个状态：
```java
    public enum State {
        IDLE, DOWNLOADING, PAUSE, FINISH;
    }
```
按钮的点击事件通过绑定，在`Presenter`的`startTask`函数中处理，绑定Button的事件：
```java
android:onClick="@{(view)->presenter.startTask(info)}"
```
当我们处于不同的状态时，一般会去改变不同的样式，这时是通过`BindingAdapter`实现的：
```java
    @BindingAdapter("update")
    public static void updateText(Button btn, Presenter.State state) {
        Log.d("zhuangsj", "updateText: " + state);
        btn.setText("" + state);
    }
```
在需要绑定的view中声明了`app:update`,并传入一个State的变量，这样Binding框架就会自动去调用`updateText`这个函数了，使用`BindingAdapter`时，函数一定要是static,第一个参数是对应的View类。
绑定`ImageInfo`的值与UI元素应用有两种方法 ，如ImageInfo.java所示的，我们可以使用ObservableField的形式，但是这种方法的变量只能声明为public，而且对应的不能有getter，好处就是不用继承一个类：
```java
    public ObservableInt progress = new ObservableInt();
    public ObservableField<Presenter.State> state = new ObservableField<>();`
```
修改数据的时候，直接set()，get()就可以了，还有一种方法是继承`BaseObservable`类,如ImageInfo1.java所示的，在对应的getter中加上注解`@Bindable`,注意要在setter中调用notifyPropertyChanged(BR.progress);这样才能实现更新：
```java
    public class ImageInfo1 extends BaseObservable {
        private int progress;
        @Bindable
        public int getProgress() {
            return progress;
        }
        
        public void setProgress(int progress) {
            this.progress = progress;
            notifyPropertyChanged(BR.progress);
        }
    }
```
在`Presenter`中启动了线程处理任务后，直接设置了`ImageInfo`的值就可以了，这样就可以直接更新到UI上了，省去了传统方式的麻烦。

# 总结
使用Data Binding的方法单条更新数据比以往的形式简单了很多，而且Data Binding是在编译时解析的，在运行时的效率比findViewbyId的方式高效很多，也省去了很多需要切换线程更新UI的麻烦。

# 感谢
+ [data-binding官方文档](https://developer.android.com/topic/libraries/data-binding/index.html) 
+ [MVVM Demo](https://github.com/luxiaoming/MVVMDemo)
+ [Android DataBinding 数据绑定](http://mp.weixin.qq.com/s?__biz=MzI1NjEwMTM4OA==&mid=2651232170&idx=1&sn=f4d7eb8f35ebf3b13696562ca3172bac&chksm=f1d9eac9c6ae63df357c3a96aa0218b5d66237c5411de5b34cd24ddb7a1d258b34444966d8c6&scene=0#rd)
