
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UseritemView extends LinearLayout {

    private ImageView image;
    private TextView name;
    private TextView contents;
    private ImageView uriImage;

    public UseritemView(Context context) {
        super(context);
        init(context);
    }

    public UseritemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.user_item,this,true);
        name = (TextView)findViewById(R.id.name);
        contents = (TextView)findViewById(R.id.contents);
        image = (ImageView)findViewById(R.id.image);
        uriImage = (ImageView)findViewById(R.id.uri);
    }

    public void setName(String name2){
        name.setText(name2);
    }

    public void setContents(String c){
        contents.setText(c);
    }

    public void setImage(int resId){
        if(resId != -1)
            image.setImageResource(resId);

    }

    public void setUri(Uri uri){
        if(uri == null)
        {
            uriImage.setImageBitmap(null);
        }
        else {
            uriImage.setImageURI(uri);
        }
    }
}
