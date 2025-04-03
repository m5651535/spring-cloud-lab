from flask import Flask, request
import subprocess
import json
import traceback
import logging
import os
import tempfile
import time

# 設置日誌
logging.basicConfig(level=logging.INFO,
                   format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

# 安裝必要的工具
if not os.path.exists("/usr/bin/curl"):
    logger.info("安裝 curl...")
    os.system("apt-get update && apt-get install -y curl")

if not os.path.exists("/usr/bin/git"):
    logger.info("安裝 git...")
    os.system("apt-get update && apt-get install -y git")

app = Flask(__name__)

@app.route('/webhook', methods=['POST'])
def webhook():
    try:
        # 記錄原始請求
        logger.info(f"收到 webhook 請求: {request.headers}")
        logger.info(f"請求內容: {request.data.decode('utf-8')}")
        
        # 嘗試解析 JSON
        try:
            data = request.get_json()
            if data is None:
                logger.error("無法解析 JSON 請求")
                return "Invalid JSON", 400
        except Exception as e:
            logger.error(f"JSON 解析失敗: {e}")
            return "Invalid JSON format", 400
        
        # 記錄解析後的數據
        logger.info(f"解析後的數據: {json.dumps(data, ensure_ascii=False)}")
        
        # 檢查是否為 master 分支的推送
        ref = data.get('ref', '')
        logger.info(f"分支 ref: {ref}")
        
        if ref == 'refs/heads/master':
            logger.info("檢測到 master 分支的推送，正在執行同步...")
            
            try:
                # 創建臨時目錄
                temp_dir = tempfile.mkdtemp()
                logger.info(f"創建臨時目錄: {temp_dir}")
                
                # 克隆 Git 倉庫
                git_url = "https://github.com/m5651535/spring-cloud-configs.git"  # 替換為您的倉庫 URL
                clone_result = subprocess.run([
                    "git", "clone", git_url, temp_dir
                ], check=True, capture_output=True, text=True)
                logger.info(f"克隆倉庫結果: {clone_result.stdout}")
                
                # 掃描配置目錄並同步到 Consul
                configs_dir = os.path.join(temp_dir, "configs")
                if os.path.exists(configs_dir):
                    for service_dir in os.listdir(configs_dir):
                        service_path = os.path.join(configs_dir, service_dir)
                        if os.path.isdir(service_path):
                            # 查找數據文件
                            data_file = None
                            for filename in ["data.yaml", "data.yml"]:
                                file_path = os.path.join(service_path, filename)
                                if os.path.isfile(file_path):
                                    data_file = file_path
                                    break
                            
                            if data_file:
                                logger.info(f"正在同步 {service_dir} 的配置到 Consul...")
                                
                                # 刪除舊鍵值
                                delete_result = subprocess.run([
                                    "curl", "-X", "DELETE", 
                                    f"http://consul:8500/v1/kv/config/{service_dir}/data"
                                ], check=True, capture_output=True, text=True)
                                logger.info(f"刪除舊鍵值結果: {delete_result.stdout}")
                                
                                # 讀取配置文件
                                with open(data_file, "r") as f:
                                    config_content = f.read()
                                
                                # 顯示文件內容以供驗證
                                logger.info(f"配置文件內容: {config_content}")
                                
                                # 上傳新配置
                                put_result = subprocess.run([
                                    "curl", "-X", "PUT", 
                                    f"http://consul:8500/v1/kv/config/{service_dir}/data", 
                                    "-d", config_content
                                ], check=True, capture_output=True, text=True)
                                logger.info(f"上傳新配置結果: {put_result.stdout}")
                                
                                # 等待 1 秒
                                time.sleep(1)
                else:
                    logger.warning(f"找不到配置目錄: {configs_dir}")
                
                # 清理臨時目錄
                subprocess.run(["rm", "-rf", temp_dir], check=True)
                
                # 設置更新標記
                flag_result = subprocess.run([
                    "curl", "-X", "PUT", 
                    "http://consul:8500/v1/kv/config/reload", 
                    "-d", str(int(time.time()))
                ], check=True, capture_output=True, text=True)
                logger.info(f"設置重載標記結果: {flag_result.stdout}")
                
                return "OK", 200
            except subprocess.CalledProcessError as e:
                logger.error(f"同步失敗: {e}")
                logger.error(f"錯誤輸出: {e.stderr}")
                return "Sync failed", 500
            except Exception as e:
                logger.error(f"處理同步時發生異常: {e}")
                logger.error(traceback.format_exc())
                return "Sync error", 500
        else:
            logger.info(f"非 master 分支的推送，已忽略: {ref}")
            return "Ignored", 200
    except Exception as e:
        # 捕獲所有其他異常
        logger.error(f"處理 webhook 時發生異常: {e}")
        logger.error(traceback.format_exc())
        return "Internal Server Error", 500

@app.route('/webhook', methods=['GET'])
def test():
    return "Webhook server is running", 200

if __name__ == "__main__":
    logger.info("Webhook 伺服器啟動在 0.0.0.0:8080")
    app.run(host="0.0.0.0", port=8080)