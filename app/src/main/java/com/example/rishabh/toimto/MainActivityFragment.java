package com.example.rishabh.toimto;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rishabh.toimto.Utilities.UrlHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 *
 * 'simple' HAHAHAHA
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView recyclerView;
    private ListAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.rec_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        adapter = new ListAdapter(ListData.getListData(), getContext());
        recyclerView.setAdapter(adapter);

        /*final EditText search_text = (EditText) v.findViewById(R.id.search_text);
        Button search_button = (Button) v.findViewById(R.id.search_button);

        // On pressing the 'Enter' key
        search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.e("Search", "Using enter key");
                    startIntent(search_text.getText().toString());
                    return true;
                }
                return false;
            }
        });

        //On clicking button
        search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("Search", "Using button");
                startIntent(search_text.getText().toString());
            }
        });
*/

        return v;
    }

    private class FetchMovies extends AsyncTask<Void, Void, Void>
    {
        Context mContext;

        FetchMovies(Context context){
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String link = "http://api.themoviedb.org/3/movie/now_playing?api_key=0202ead4ea370b743c58c0e863ff6bd9";
            String data = UrlHelper.getRequest(link, mContext);
            Log.e("Data", data);
            return null;
        }
    }


    private static class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder>{

        private LayoutInflater inflater;
        private List<ListItem> list;

        public ListAdapter(List<ListItem> list, Context context){
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.card_item, parent, false);
            return new ListHolder(view);
        }

        @Override
        public void onBindViewHolder(ListHolder holder, int position){
            //Log.e("item", item.getText()+" : "+item.getImage()+" : "+position);
            ListItem item = list.get(position);
            holder.text.setText(item.getText());
            holder.image.setImageResource(item.getImage());
        }

        @Override
        public int getItemCount() {
            return ListData.text.length * 5;
        }

        class ListHolder extends RecyclerView.ViewHolder{

            private TextView text;
            private ImageView image;
            private View container;

            public ListHolder(View itemView) {
                super(itemView);

                text = (TextView) itemView.findViewById(R.id.card_text);
                image = (ImageView) itemView.findViewById(R.id.card_image);

                container = itemView.findViewById(R.id.card_root);


            }
        }

    }

    private static class ListData{

        public final static String[] text = {"Suicide Squad", "Jason Bourne", "Batman: The Killing Joke"};
        public final static int[] image = {R.drawable.test_poster,
                R.drawable.test_poster2,
                R.drawable.test_poster3};

        public static List<ListItem> getListData(){
            List<ListItem> data = new ArrayList<ListItem>();

            for(int x=0; x<5; x++){
                for(int y=0; y<text.length; y++){
                    data.add(new ListItem(text[y], image[y]));
                }
            }

            return data;

        }
    }

    private static class ListItem{

        private String text;
        private int image;

        public ListItem(String text, int image){
            setText(text);
            setImage(image);
        }

        public ListItem(){}

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String toString() {return getText()+" : "+getImage();}
    }

}











// Sample JSON String retrieved
/*{
   "page":1,
   "results":[
      {
         "poster_path":"\/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg",
         "adult":false,
         "overview":"From DC Comics comes the Suicide Squad, an antihero team of incarcerated supervillains who act as deniable assets for the United States government, undertaking high-risk black ops missions in exchange for commuted prison sentences.",
         "release_date":"2016-08-03",
         "genre_ids":[
            28,
            80,
            878
         ],
         "id":297761,
         "original_title":"Suicide Squad",
         "original_language":"en",
         "title":"Suicide Squad",
         "backdrop_path":"\/ndlQ2Cuc3cjTL7lTynw6I4boP4S.jpg",
         "popularity":58.227091,
         "vote_count":950,
         "video":false,
         "vote_average":5.97
      },
      {
         "poster_path":"\/lFSSLTlFozwpaGlO31OoUeirBgQ.jpg",
         "adult":false,
         "overview":"The most dangerous former operative of the CIA is drawn out of hiding to uncover hidden truths about his past.",
         "release_date":"2016-07-27",
         "genre_ids":[
            28,
            53
         ],
         "id":324668,
         "original_title":"Jason Bourne",
         "original_language":"en",
         "title":"Jason Bourne",
         "backdrop_path":"\/AoT2YrJUJlg5vKE3iMOLvHlTd3m.jpg",
         "popularity":23.51064,
         "vote_count":484,
         "video":false,
         "vote_average":5.26
      },
      {
         "poster_path":"\/3S7V2Jd2G61LltoCsYUj4GwON5p.jpg",
         "adult":false,
         "overview":"A woman with a seemingly perfect life - a great marriage, overachieving kids, beautiful home, stunning looks and still holding down a career. However she's over-worked, over committed and exhausted to the point that she's about to snap. Fed up, she joins forces with two other over-stressed moms and all go on a quest to liberate themselves from conventional responsibilities, going on a wild un-mom like binge of freedom, fun and self-indulgence - putting them on a collision course with PTA Queen Bee Gwendolyn and her clique of devoted perfect moms.",
         "release_date":"2016-07-28",
         "genre_ids":[
            35
         ],
         "id":376659,
         "original_title":"Bad Moms",
         "original_language":"en",
         "title":"Bad Moms",
         "backdrop_path":"\/l9aqTBdafSo0n7u0Azuqo01YVIC.jpg",
         "popularity":7.027125,
         "vote_count":78,
         "video":false,
         "vote_average":5.51
      },
      {
         "poster_path":"\/e9Rzr8Hhu3pqdJtdDLC52PerLk1.jpg",
         "adult":false,
         "overview":"Pete is a mysterious 10-year-old with no family and no home who claims to live in the woods with a giant, green dragon named Elliott. With the help of Natalie, an 11-year-old girl whose father Jack owns the local lumber mill, forest ranger Grace sets out to determine where Pete came from, where he belongs, and the truth about this dragon.",
         "release_date":"2016-08-10",
         "genre_ids":[
            12,
            10751,
            14
         ],
         "id":294272,
         "original_title":"Pete's Dragon",
         "original_language":"en",
         "title":"Pete's Dragon",
         "backdrop_path":"\/AaRhHX0Jfpju0O6hNzScPRgX9Mm.jpg",
         "popularity":5.89291,
         "vote_count":38,
         "video":false,
         "vote_average":4.28
      },
      {
         "poster_path":"\/zm0ODjtfJfJW0W269LqsQl5OhJ8.jpg",
         "adult":false,
         "overview":"As Batman hunts for the escaped Joker, the Clown Prince of Crime attacks the Gordon family to prove a diabolical point mirroring his own fall into madness. Based on the graphic novel by Alan Moore and Brian Bolland.",
         "release_date":"2016-07-24",
         "genre_ids":[
            28,
            16,
            80,
            18
         ],
         "id":382322,
         "original_title":"Batman: The Killing Joke",
         "original_language":"en",
         "title":"Batman: The Killing Joke",
         "backdrop_path":"\/7AxMc1Mgm3xD2lySdM6r0sQGS3s.jpg",
         "popularity":4.930017,
         "vote_count":121,
         "video":false,
         "vote_average":6.01
      },
      {
         "poster_path":"\/wJXku1YhMKeuzYNEHux7XtaYPsE.jpg",
         "adult":false,
         "overview":"Based on a true story, “War Dogs” follows two friends in their early 20s living in Miami during the first Iraq War who exploit a little-known government initiative that allows small businesses to bid on U.S. Military contracts.  Starting small, they begin raking in big money and are living the high life.  But the pair gets in over their heads when they land a 300 million dollar deal to arm the Afghan Military—a deal that puts them in business with some very shady people, not the least of which turns out to be the U.S. Government.",
         "release_date":"2016-08-18",
         "genre_ids":[
            10752,
            35,
            18
         ],
         "id":308266,
         "original_title":"War Dogs",
         "original_language":"en",
         "title":"War Dogs",
         "backdrop_path":"\/2cLndRZy8e3das3vVaK3BdJfRIi.jpg",
         "popularity":4.676365,
         "vote_count":12,
         "video":false,
         "vote_average":5.29
      },
      {
         "poster_path":"\/3ioyAtm0wXDyPy330Y7mJAJEHpU.jpg",
         "adult":false,
         "overview":"A high school senior finds herself immersed in an online game of truth or dare, where her every move starts to be manipulated by an anonymous community of \"watchers.\"",
         "release_date":"2016-07-27",
         "genre_ids":[
            18,
            53
         ],
         "id":328387,
         "original_title":"Nerve",
         "original_language":"en",
         "title":"Nerve",
         "backdrop_path":"\/a0wohltYr7Tzkgg2X6QKBe3txj1.jpg",
         "popularity":4.331216,
         "vote_count":47,
         "video":false,
         "vote_average":6.33
      },
      {
         "poster_path":"\/3Kr9CIIMcXTPlm6cdZ9y3QTe4Y7.jpg",
         "adult":false,
         "overview":"In the epic fantasy, scruffy, kindhearted Kubo ekes out a humble living while devotedly caring for his mother in their sleepy shoreside village. It is a quiet existence – until a spirit from the past catches up with him to enforce an age-old vendetta. Suddenly on the run from gods and monsters, Kubo’s chance for survival rests on finding the magical suit of armor once worn by his fallen father, the greatest samurai the world has ever known. Summoning courage, Kubo embarks on a thrilling odyssey as he faces his family’s history, navigates the elements, and bravely fights for the earth and the stars.",
         "release_date":"2016-08-18",
         "genre_ids":[
            12,
            16,
            14,
            10751
         ],
         "id":313297,
         "original_title":"Kubo and the Two Strings",
         "original_language":"en",
         "title":"Kubo and the Two Strings",
         "backdrop_path":"\/qymILRKAeo4srgYG3fehPanOCvO.jpg",
         "popularity":4.114674,
         "vote_count":10,
         "video":false,
         "vote_average":5.25
      },
      {
         "poster_path":"\/tgfRDJs5PFW20Aoh1orEzuxW8cN.jpg",
         "adult":false,
         "overview":"Arthur Bishop thought he had put his murderous past behind him when his most formidable foe kidnaps the love of his life. Now he is forced to travel the globe to complete three impossible assassinations, and do what he does best, make them look like accidents.",
         "release_date":"2016-08-25",
         "genre_ids":[
            80,
            28,
            53
         ],
         "id":278924,
         "original_title":"Mechanic: Resurrection",
         "original_language":"en",
         "title":"Mechanic: Resurrection",
         "backdrop_path":"\/AdbXsuifp0VEnZgJ3vu5TFU8ms3.jpg",
         "popularity":4.048111,
         "vote_count":54,
         "video":false,
         "vote_average":4.24
      },
      {
         "poster_path":"\/mxyosE7j1ilAYPpHP92cfSjTEou.jpg",
         "adult":false,
         "overview":"It's pedal to the metal as Scooby-Doo, Shaggy and the gang team up with the superstars of WWE in this hi-octane, all-new original movie! When Scooby and Mystery Inc. visit an off-road racing competition, it's not long before strange events start to occur. A mysterious phantom racer, known only as Inferno, is causing chaos and determined to sabotage the race. It's up Scooby-Doo, Shaggy and their new driving partner, The Undertaker, to save the race and solve the mystery. Along with other WWE superstars such as Triple H, Paige and Shamus, it's time to start your engine and your appetite because Scooby-Doo and WWE are chasing down adventure and laughs just for you!",
         "release_date":"2016-08-09",
         "genre_ids":[
            16
         ],
         "id":409122,
         "original_title":"Scooby-Doo! And WWE: Curse of the Speed Demon",
         "original_language":"en",
         "title":"Scooby-Doo! And WWE: Curse of the Speed Demon",
         "backdrop_path":"\/lmiFdQ2uiYvdrKHo7frSXegjCVV.jpg",
         "popularity":3.85973,
         "vote_count":2,
         "video":false,
         "vote_average":6
      },
      {
         "poster_path":"\/plmsWjJSVo0tEHLqjR6M8pu2vEI.jpg",
         "adult":false,
         "overview":"Village lad Sarman is drawn to big, bad Mohenjo Daro - and its mascot Chaani. But Chaani must wed Munja, son of Mohenjo Daro's ruler, Maham. Will Sarman find love - and more - in Mohenjo Daro?",
         "release_date":"2016-08-12",
         "genre_ids":[
            12,
            18,
            36,
            10749
         ],
         "id":402672,
         "original_title":"Mohenjo Daro",
         "original_language":"hi",
         "title":"Mohenjo Daro",
         "backdrop_path":"\/wUMbhjcPNyX1qu5nAimHQxe4xLB.jpg",
         "popularity":3.279093,
         "vote_count":4,
         "video":false,
         "vote_average":4.13
      },
      {
         "poster_path":"\/5gacorDncUH8T66Tay7OJMAXLD0.jpg",
         "adult":false,
         "overview":"Guy Carter, an insecure expectant father unable to find work in his field, accepts a job driving hookers around Los Angeles. One long and crazy evening proves to our hero that he is, in fact, up to the task of fatherhood.",
         "release_date":"2016-08-05",
         "genre_ids":[
            35
         ],
         "id":376581,
         "original_title":"Amateur Night",
         "original_language":"en",
         "title":"Amateur Night",
         "backdrop_path":"\/2CQE9imcp8GCeBHIHmxh3k9tG9J.jpg",
         "popularity":3.205784,
         "vote_count":6,
         "video":false,
         "vote_average":5.75
      },
      {
         "poster_path":"\/rxXA5vwJElXQ8BgrB0pocUcuqFA.jpg",
         "adult":false,
         "overview":"When Rebecca left home, she thought she left her childhood fears behind. Growing up, she was never really sure of what was and wasn’t real when the lights went out…and now her little brother, Martin, is experiencing the same unexplained and terrifying events that had once tested her sanity and threatened her safety. A frightening entity with a mysterious attachment to their mother, Sophie, has reemerged.",
         "release_date":"2016-07-22",
         "genre_ids":[
            27
         ],
         "id":345911,
         "original_title":"Lights Out",
         "original_language":"en",
         "title":"Lights Out",
         "backdrop_path":"\/bFte6pKyTUBVnHCQ481VVjvh3zP.jpg",
         "popularity":3.185082,
         "vote_count":108,
         "video":false,
         "vote_average":5.86
      },
      {
         "poster_path":"\/nfkF6fow0cWdMIN19WbGngqDz2o.jpg",
         "adult":false,
         "overview":"A falsely accused nobleman survives years of slavery to take vengeance on his best friend who betrayed him.",
         "release_date":"2016-08-17",
         "genre_ids":[
            12,
            36,
            18
         ],
         "id":271969,
         "original_title":"Ben-Hur",
         "original_language":"en",
         "title":"Ben-Hur",
         "backdrop_path":"\/A4xbEpe9LevQCdvaNC0z6r8AfYk.jpg",
         "popularity":3.15964,
         "vote_count":25,
         "video":false,
         "vote_average":2.52
      },
      {
         "poster_path":"\/zJSsTlkobUglQNQCTcyjUkwddEY.jpg",
         "adult":false,
         "overview":"Nate Foster, a young, idealistic FBI agent, goes undercover to take down a radical white supremacy terrorist group. The bright up-and-coming analyst must confront the challenge of sticking to a new identity while maintaining his real principles as he navigates the dangerous underworld of white supremacy. Inspired by real events.",
         "release_date":"2016-08-19",
         "genre_ids":[
            18,
            53,
            80
         ],
         "id":374617,
         "original_title":"Imperium",
         "original_language":"en",
         "title":"Imperium",
         "backdrop_path":"\/zPastwYrItjjc6eXr12J8sDPu0n.jpg",
         "popularity":2.984673,
         "vote_count":7,
         "video":false,
         "vote_average":3
      },
      {
         "poster_path":"\/274TgZ1h6ZDdjVIOamoyDoMe59k.jpg",
         "adult":false,
         "overview":"In Pursuit of Peace follows four Canadians on the front lines of international peace initiatives - in South Sudan, Turkey, Congo and Iraq. We experience the challenges of their work, exploring how their peace building strategies are put to the test in this new 21st century paradigm of conflict resolution.",
         "release_date":"2016-08-19",
         "genre_ids":[
            99
         ],
         "id":411807,
         "original_title":"In Pursuit of Peace",
         "original_language":"en",
         "title":"In Pursuit of Peace",
         "backdrop_path":null,
         "popularity":2.9223,
         "vote_count":0,
         "video":false,
         "vote_average":0
      },
      {
         "poster_path":"\/sO61kOJEObDs8uOTkQLj5j8eaxu.jpg",
         "adult":false,
         "overview":"Elite snipers Brandon Beckett (Chad Michael Collins) and Richard Miller (Billy Zane) tasked with protecting a gas pipeline from terrorists looking to make a statement. When battles with the enemy lead to snipers being killed by a ghost shooter who knows their exact location, tensions boil as a security breach is suspected. Is there someone working with the enemy on the inside? Is the mission a front for other activity? Is the Colonel pulling the strings?",
         "release_date":"2016-08-02",
         "genre_ids":[
            10752,
            28,
            18
         ],
         "id":407375,
         "original_title":"Sniper: Ghost Shooter",
         "original_language":"en",
         "title":"Sniper: Ghost Shooter",
         "backdrop_path":"\/yYS8wtp7PgRcugt6EUMhv95NnaK.jpg",
         "popularity":2.881024,
         "vote_count":12,
         "video":false,
         "vote_average":4.38
      },
      {
         "poster_path":"\/u2e80wuj0pCXiejWCynDGVm4Bum.jpg",
         "adult":false,
         "overview":"'Man of Tomorrow' is a narrative experiment that combines Man of Steel and Batman V. Superman into one film. The goal was to use material from both films to form a single, feature-length, 3-act structure that focuses on Superman.",
         "release_date":"2016-08-16",
         "genre_ids":[
            28
         ],
         "id":411790,
         "original_title":"Man of Tomorrow",
         "original_language":"en",
         "title":"Man of Tomorrow : A Man of Steel and Batman V Superman FanEdit",
         "backdrop_path":null,
         "popularity":2.8556,
         "vote_count":1,
         "video":false,
         "vote_average":9
      },
      {
         "poster_path":"\/uoB5FlrTvRobm0n7adrDOWEEheL.jpg",
         "adult":false,
         "overview":"Rustom Pavri, an honourable officer of the Indian Navy shoots his friend Vikram  to death after discovering that his wife Cynthia had an affair with the rich businessman. The Commander surrenders himself to the Police immediately and admits to have killed Vikram but pleads 'not guilty' in court. Is he convicted or acquitted?",
         "release_date":"2016-08-12",
         "genre_ids":[
            53,
            10749
         ],
         "id":392572,
         "original_title":"Rustom",
         "original_language":"hi",
         "title":"Rustom",
         "backdrop_path":"\/8WT907al6ecrXNcx1fNEgtaGvHQ.jpg",
         "popularity":2.844297,
         "vote_count":4,
         "video":false,
         "vote_average":7.63
      },
      {
         "poster_path":"\/wMeZGTdM1BqsQyGqC9BY09p2Ye9.jpg",
         "adult":false,
         "overview":"Based on an extremely popular Chinese internet novel, SWEET SIXTEEN (XIA YOU QIAO MU) stars Kris Wu, Han Geng and Joo Won as three men troubled by their own pasts; intertwined among the men is each’s specific relationship with Shu Yamang.  She is a rock in their past, present and future and her life is influenced by the trials and tribulations of these three wandering souls.",
         "release_date":"2016-08-17",
         "genre_ids":[
            18,
            10749
         ],
         "id":411757,
         "original_title":"Sweet Sixteen (Xia You Qiao Mu)",
         "original_language":"en",
         "title":"Sweet Sixteen",
         "backdrop_path":null,
         "popularity":2.8291,
         "vote_count":1,
         "video":false,
         "vote_average":10
      }
   ],
   "dates":{
      "maximum":"2016-08-26",
      "minimum":"2016-07-15"
   },
   "total_pages":29,
   "total_results":561
}*/