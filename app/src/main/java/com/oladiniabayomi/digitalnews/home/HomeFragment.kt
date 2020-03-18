package com.oladiniabayomi.digitalnews.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.viewpager.widget.ViewPager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.oladiniabayomi.digitalarticles.articles.Articles
import com.oladiniabayomi.digitalnews.custom.MyPageIndicator
import com.oladiniabayomi.digitalnews.R
import com.oladiniabayomi.digitalnews.articles.ArticlesRecyclerViewAdapter
import com.oladiniabayomi.digitalnews.detailed_article.DetailedArticleActivity
import com.oladiniabayomi.digitalnews.featured.FeaturedFragment
import com.oladiniabayomi.digitalnews.interfaces.OnItemClickListener
import java.lang.Exception

class HomeFragment : Fragment(), OnItemClickListener {

    private lateinit var homeViewModel: HomeViewModel
    //RecyclerView Variables
    lateinit var recyclerView: RecyclerView
    private var articlesRecyclerViewAdapter: ArticlesRecyclerViewAdapter? = null
    private var layoutManager: LayoutManager? = null
    private var currentArticles = ArrayList<Articles>()

    //ViewPager variables
    var mViewPager: ViewPager? = null;
    var mLinearLayout: LinearLayout? = null
    var mAdapter: CustomPagerAdapter2? = null
    var mIndicator: MyPageIndicator? = null

    private lateinit var skeleton: Skeleton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        /*for ( x in 0..5 ){
            fragments.add(FeaturedFragment().newInstance("https://i2.wp.com/www.tell.com.ng/wp-content/uploads/2020/02/images-1-1.jpeg?fit=610%2C503&ssl=1",
                "Loading"))
        }*/
        initialization(root)

        homeViewModel.allArticles.observe(viewLifecycleOwner, Observer { articles ->
            articles.let { articlesRecyclerViewAdapter!!.setArticles(ArrayList(it)) }
        })

        var fragments = ArrayList<Fragment>(5)

        fragments.add(
            FeaturedFragment().newInstance(
                R.drawable.loading.toString(),
                " "
            )
        )
        fragments.add(
            FeaturedFragment().newInstance(
                R.drawable.loading.toString(),
                " "
            )
        )
        fragments.add(
            FeaturedFragment().newInstance(
                R.drawable.loading.toString(),
                " "
            )
        )
        fragments.add(
            FeaturedFragment().newInstance(
                R.drawable.loading.toString(),
                " "
            )
        )
        fragments.add(
            FeaturedFragment().newInstance(
                R.drawable.loading.toString(),
                " "
            )
        )


        mAdapter = CustomPagerAdapter2(activity!!.supportFragmentManager, fragments)
        mViewPager!!.adapter = mAdapter



        mIndicator = MyPageIndicator(
            activity!!.applicationContext,
            mLinearLayout!!,
            mViewPager!!,
            R.drawable.tab_selector
        )

        mIndicator!!.setPageCount(fragments.size)


        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (!isConnected){
            mViewPager!!.visibility = View.INVISIBLE

        }else{

            mViewPager!!.visibility = View.VISIBLE
            mIndicator!!.show()

        }



        //skeleton.showSkeleton()
        homeViewModel.allCategories.observe(viewLifecycleOwner, Observer { articles ->
            /*Toast.makeText(context, articles[1].articlesFullText!!.rendered , Toast.LENGTH_LONG)
                .show()*/
            if (articles != null) {
                fragments.clear()
                for (x in 0 until 5) {
                    try {
                        fragments.add(
                            FeaturedFragment().newInstance(
                                articles[x].articlesThumbnailImage!!,
                                articles[x].articlesTitle!!.rendered!!
                            )
                        )
                    } catch (e: Exception) {
                    }
                }
            } else {
                mLinearLayout!!.visibility = View.INVISIBLE
            }
            mAdapter!!.notifyDataSetChanged()
        })
        if (isConnected) {

        } else {
            Toast.makeText(
                context,
                "No Internet, Please check your Internet Connection",
                Toast.LENGTH_LONG
            )
                .show()
        }


        return root
    }


    override fun onItemClick(articles: Articles) {
        val intent = Intent(activity, DetailedArticleActivity::class.java)
        intent.putExtra("articles", articles)
        startActivity(intent)
    }


    fun initialization(root: View) {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // initialization
        mViewPager = root.findViewById(R.id.viewPager)
        mLinearLayout = root.findViewById(R.id.pagesContainer)
        recyclerView = root.findViewById(R.id.home_recyclerview)

        // Either use an existing Skeletonlayout
        skeleton = recyclerView.applySkeleton(R.layout.article_item_view, 10)

        articlesRecyclerViewAdapter =
            context?.let { ArticlesRecyclerViewAdapter(it, currentArticles, this) }

        //setting Recycler Views attribute
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = articlesRecyclerViewAdapter

    }


    fun addCategories() {

    }


    fun addFragments() {

    }

    class CustomPagerAdapter2 :
        FragmentStatePagerAdapter {

        var mFrags: List<Fragment> = ArrayList()

        constructor(fm: FragmentManager, frags: List<Fragment>) : super(fm){
            mFrags = frags
        }

        override fun getItem(position: Int): Fragment {
            var index: Int = position % mFrags.size
            return mFrags[index]

        }
        override fun getCount(): Int {
            return Integer.MAX_VALUE
        }

/*
        override fun getRealCount(): Int {
            return mFrags.size
        }
*/
    }
}