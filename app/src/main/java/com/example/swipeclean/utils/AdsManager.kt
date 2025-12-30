package com.example.swipeclean.utils

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdsManager(private val context: Context) {

    private var interstitialAd: InterstitialAd? = null

    fun initialize() {
        MobileAds.initialize(context)
    }

    fun loadInterstitial(adUnit: String) {
        val request = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            adUnit,
            request,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(e: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    fun showInterstitial(activity: Activity, onDismiss: () -> Unit) {
        interstitialAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    onDismiss()
                    interstitialAd = null
                }
            }

        interstitialAd?.show(activity)
            ?: onDismiss() // If ad not ready, continue delete
    }

    fun getBanner(): AdView {
        return AdView(context).apply {
            adUnitId = "ca-app-pub-3940256099942544/6300978111" // test
            setAdSize(AdSize.BANNER)
            loadAd(AdRequest.Builder().build())
        }
    }
}
