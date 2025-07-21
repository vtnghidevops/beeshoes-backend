# 🚀 Backend CI/CD Workflows

## 📋 Tóm tắt

Hệ thống CI/CD đơn giản với 2 workflow chính:

### 🔄 1. Development Auto Deploy (`backend-development.yml`)

- **Trigger 1**: Pull Request merged vào branch `develop`
- **Trigger 2**: Push Git tag `dev-v*` (manual từ dev lead)
- **Chức năng**: Build image mới → Push Git tag (nếu cần) → Deploy lên dev environment
- **Git tag**: `dev-v0.1.2` (auto increment hoặc existing tag)
- **Image tag**: `devops/backend:dev-a1b2c3d` (luôn dùng commit hash)

### 🎯 2. Staging/Production Promote (`backend-staging.yml`)

- **Trigger**: Manual từ GitHub UI
- **Chức năng**: Promote image từ dev → staging/prod (không build lại) → **Push Git tag**
- **Git tag**: `stag-v1.2.3` hoặc `prod-v2.0.1` (manual version bump)
- **Image tag**: `devops/backend:stag-v1.2.3` hoặc `devops/backend:prod-v2.0.1`

## 🎯 Cách sử dụng

### Development (Tự động)

```bash
# Developers chỉ cần push code lên develop
git checkout develop
git add .
git commit -m "feat: new feature"
git push origin develop
```

→ **Tự động** build image + tạo Git tag + deploy lên dev environment

**Kết quả**:

- ✅ Tạo Git tag: `dev-v0.1.2` (auto increment)
- ✅ Build image: `devops/backend:dev-a1b2c3d` (commit hash)
- ✅ Update Helm chart `envs/dev/dev-values.yaml`
- ✅ Deploy lên development

### Staging/Production (Manual - Dev Lead)

1. **Vào GitHub Actions** → `Backend Staging/Production Deployment` → `Run workflow`

2. **Nhập thông tin**:

   - **Source image**: `dev-a1b2c3d` (chọn từ dev images có sẵn)
   - **Target environment**: `stag` hoặc `prod`
   - **Version bump**: `patch`, `minor`, hoặc `major`

3. **Kết quả**:
   - ✅ Promote image: `devops/backend:stag-v1.2.3`
   - ✅ Tạo Git tag: `stag-v1.2.3` (với version bump)
   - ✅ Update Helm chart cho target environment
   - ✅ Deploy lên staging/production

## 🏷️ Image Tag Examples

| Environment | Format          | Example                      |
| ----------- | --------------- | ---------------------------- |
| Development | `dev-{hash}`    | `devops/backend:dev-a1b2c3d` |
| Staging     | `stag-v{x.y.z}` | `devops/backend:stag-v1.2.3` |
| Production  | `prod-v{x.y.z}` | `devops/backend:prod-v2.0.1` |

## 🔧 Repository Secrets cần cấu hình

```yaml
# Docker Registry
REGISTRY: "your-registry.com"
DOCKER_USERNAME: "username"
DOCKER_PASSWORD: "password"

# Helm Chart Repository
HELM_CHAR_TOKEN: "github_pat_xxx"
HELM_CHAR_USERNAME: "helm-repo-owner"
HELM_CHAR_REPO: "helm-chart-repo-name"

# Git Configuration
GIT_USERNAME: "CI Bot"
GIT_EMAIL: "ci@company.com"
```

## 📁 Helm Chart Structure

```
helm-chart-repo/
├── envs/
│   ├── dev/
│   │   └── dev-values.yaml
│   ├── staging/
│   │   └── staging-values.yaml
│   └── prod/
│       └── prod-values.yaml
```

## 🛡️ Branch Protection (Tương lai)

- **Developers**: Chỉ push lên `develop`
- **Dev Lead**: Quyền manual deploy staging/prod
- **Main/Release branches**: Protected, cần approval

## 📁 Files cần thiết cho Flow đầy đủ

### 1. GitHub Workflows (`.github/workflows/`)

```
beeshoes-backend/
├── .github/
│   └── workflows/
│       ├── backend-development.yml      # Auto deploy dev
│       ├── backend-staging.yml          # Manual promote staging/prod
│       └── README.md                    # Hướng dẫn sử dụng
```

### 2. Application Files

```
beeshoes-backend/
├── Dockerfile                           # Docker build config
├── src/                                 # Source code
├── pom.xml (hoặc package.json)         # Dependencies
└── ...
```

### 3. Helm Chart Repository (Separate repo)

```
helm-chart-repo/
├── envs/
│   ├── dev/
│   │   └── dev-values.yaml             # Dev environment config
│   ├── staging/
│   │   └── staging-values.yaml         # Staging environment config
│   └── prod/
│       └── prod-values.yaml            # Production environment config
├── templates/
│   ├── deployment.yaml
│   ├── service.yaml
│   └── ...
├── Chart.yaml
└── values.yaml                         # Base values
```

### 4. Repository Secrets (GitHub Settings)

- `REGISTRY` - Docker registry URL
- `DOCKER_USERNAME` - Docker registry username
- `DOCKER_PASSWORD` - Docker registry password
- `HELM_CHAR_TOKEN` - GitHub token for helm repo
- `HELM_CHAR_USERNAME` - Helm repo owner
- `HELM_CHAR_REPO` - Helm repo name
- `GIT_USERNAME` - Git commit username
- `GIT_EMAIL` - Git commit email

### 5. Branch Structure

```
develop  ← Developers push here (auto deploy dev)
main     ← Protected (staging/prod deploys from here)
```

## 🔍 Tracking Git Tags

**MỌI deployment đều tạo Git tag để tracking version:**

```bash
# List all tags by environment
git tag -l "dev-*"      # Development: dev-v0.1.0, dev-v0.1.1, ...
git tag -l "stag-*"     # Staging: stag-v1.0.0, stag-v1.1.0, ...
git tag -l "prod-*"     # Production: prod-v2.0.0, prod-v2.0.1, ...

# Get latest version for environment
git tag -l "prod-*" | sort -V | tail -n1      # Latest production
git tag -l "dev-*" | sort -V | tail -n1       # Latest development

# View tag details
git show dev-v0.1.5     # Shows tag info + commit details
```

## 🚀 Flow Summary

1. **Development**: Push code → **Auto create tag** → Build image → Deploy
2. **Staging/Production**: Manual input → **Create release tag** → Promote image → Deploy

---

💡 **Workflow đơn giản**: Dev code → Auto dev deploy → Dev Lead manual promote → Production
