import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finaldicoding.R
import android.view.animation.AnimationUtils;
import com.example.finaldicoding.Kpop


class ListGambar(private val context: Context, private val listGambar:ArrayList<Kpop>) : RecyclerView.Adapter<ListGambar.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var mediaPlayer: MediaPlayer? = null

    init {

        mediaPlayer = MediaPlayer.create(context, R.raw.bts)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.name)
        val imgPhoto: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_gambar, parent, false)
        view.setAnimation(AnimationUtils.loadAnimation(parent.getContext(), R.anim.slide_right));
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listGambar.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, description, photos) = listGambar[position]
        holder.imgPhoto.setImageResource(photos)
        holder.tvName.text = name

        holder.itemView.setOnClickListener {
            mediaPlayer?.start()
            onItemClickCallback.onItemClicked(listGambar[holder.adapterPosition])
        }


        holder.itemView.setOnClickListener {
            val audioName = listGambar[holder.adapterPosition]
            val audioResourceId = holder.itemView.context.resources.getIdentifier(audioName.sound, "raw", holder.itemView.context.packageName)

            mediaPlayer?.apply {
                reset()
                val context = holder.itemView.context
                val fd = context.resources.openRawResourceFd(audioResourceId)
                setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
                fd.close()
                prepare()
                start()
            }

            onItemClickCallback.onItemClicked(listGambar[holder.adapterPosition])
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        // Release MediaPlayer resources
        mediaPlayer?.release()
        mediaPlayer = null
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Kpop)
    }
}
