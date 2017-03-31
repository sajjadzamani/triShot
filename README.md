# triShot
An Android shooter game</br>
## demo
https://vimeo.com/210962482

## code


 
- All animations done in animation thread:
```java
class InkThread extends Thread
```
- Constructor for surface view</br>
Handler is initiated here for receiveing messages from animation thread
```java
public InkView(Context context, AttributeSet atrs) {
        super(context, atrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        thread = new InkThread(holder,context,new Handler() {
            @Override
            public void handleMessage(Message m) {
                btnRetry.setVisibility(m.getData().getInt("viz"));
                btnRetry.getBackground().setAlpha(150);
            }
        });
        setFocusable(true);
    }

```


